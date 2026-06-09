#include <iostream>
#include <filesystem>
#include <vector>
#include <algorithm>
#include <atomic>
#include <mutex>
#include <condition_variable>
#include "h/FileClearUtils.h"
#include <thread>
#include "../spdlog/include/spdlog/spdlog.h"

namespace fs = std::__fs::filesystem;

static std::atomic<bool> g_stop_flag{false};
static std::mutex g_stop_mutex;
static std::condition_variable g_stop_cv;

struct FileEntry {
    fs::path path;
    std::chrono::system_clock::time_point mtime;
};

void doWork(const std::string &log_dir, jlong max_cache_time, bool keep_latest_file) {
    auto now = std::chrono::system_clock::now();
    auto file_clock_now = fs::file_time_type::clock::now();

    std::vector<FileEntry> expired_files;

    std::error_code ec;
    auto dir_iter = fs::directory_iterator(log_dir, ec);
    if (ec) return;

    for (const auto &entry: dir_iter) {
        if (!entry.is_regular_file()) continue;
        try {
            auto ftime = fs::last_write_time(entry);
            auto sctp = std::chrono::time_point_cast<std::chrono::system_clock::duration>(
                    ftime - file_clock_now + now
            );
            auto time_ms = std::chrono::duration_cast<std::chrono::milliseconds>(
                    now - sctp
            ).count();

            if (time_ms > static_cast<std::chrono::milliseconds::rep>(max_cache_time)) {
                expired_files.push_back({entry.path(), sctp});
            }
        } catch (const std::exception &e) {
            spdlog::log(
                    spdlog::level::err,
                    "{}  [{}] Error processing file: {} - {}", "E",
                    "LogFileDeleteCache",
                    entry.path().string(),
                    e.what()
            );
        }
    }

    if (expired_files.empty()) return;

    if (keep_latest_file && expired_files.size() > 1) {
        std::sort(expired_files.begin(), expired_files.end(),
                  [](const FileEntry &a, const FileEntry &b) {
                      return a.mtime < b.mtime;
                  });
        expired_files.pop_back();
    } else if (keep_latest_file && expired_files.size() == 1) {
        return;
    }

    for (const auto &file: expired_files) {
        try {
            fs::remove(file.path);
            spdlog::log(
                    spdlog::level::info,
                    "{}  [{}] Deleted file: {}", "I",
                    "LogFileDeleteCache",
                    file.path.string()
            );
        } catch (const std::exception &e) {
            spdlog::log(
                    spdlog::level::err,
                    "{}  [{}] Error deleting file: {} - {}", "E",
                    "LogFileDeleteCache",
                    file.path.string(),
                    e.what()
            );
        }
    }
}

void pollingTask(const std::string &log_dir, jlong max_cache_time, jlong cleanTaskIntervalTime) {
    while (true) {
        std::unique_lock<std::mutex> lock(g_stop_mutex);
        bool stopped = g_stop_cv.wait_for(
                lock,
                std::chrono::milliseconds(cleanTaskIntervalTime),
                [] { return g_stop_flag.load(std::memory_order_relaxed); }
        );
        if (stopped) break;
        doWork(log_dir, max_cache_time, true);
    }
}

void clean_old_logs(const std::string &log_dir, jlong max_cache_time, jlong cleanTaskIntervalTime) {
    doWork(log_dir, max_cache_time, false);
    g_stop_flag.store(false, std::memory_order_relaxed);
    std::thread(pollingTask, log_dir, max_cache_time, cleanTaskIntervalTime).detach();
}

void stop_clean_task() {
    {
        std::lock_guard<std::mutex> lock(g_stop_mutex);
        g_stop_flag.store(true, std::memory_order_relaxed);
    }
    g_stop_cv.notify_all();
}


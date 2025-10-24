#include <iostream>
#include <filesystem>
#include "h/FileClearUtils.h"
#include <thread>
#include "../spdlog/include/spdlog/spdlog.h"


void doWork(const std::string &basicString, jlong time, bool keep_latest_file);

namespace fs = std::__fs::filesystem;

void pollingTask(const std::string &log_dir, jlong max_cache_time, jlong cleanTaskIntervalTime) {
    while (true) {
        std::this_thread::sleep_for(std::chrono::milliseconds(cleanTaskIntervalTime));
        doWork(log_dir, max_cache_time, true);
    }
}

void clean_old_logs(const std::string &log_dir, jlong max_cache_time, jlong cleanTaskIntervalTime) {
    doWork(log_dir, max_cache_time, false);
    std::thread(pollingTask, log_dir, max_cache_time, cleanTaskIntervalTime).detach();
}


void doWork(const std::string &log_dir, jlong max_cache_time, bool keep_latest_file) {
    spdlog::log(spdlog::level::info,
                "{}  [{}] Start deleting files that have exceeded the cache time. cacheTime={}",
                "I", "LogFileDeleteCache",
                max_cache_time
    );

    auto now = std::chrono::system_clock::now();
    fs::path latest_file;
    std::chrono::system_clock::time_point latest_time;

    for (const auto &entry: fs::directory_iterator(log_dir)) {
        if (entry.is_regular_file()) {
            try {
                auto ftime = fs::last_write_time(entry);
                auto sctp = std::chrono::time_point_cast<std::chrono::system_clock::duration>(
                        ftime - decltype(ftime)::clock::now() + std::chrono::system_clock::now()
                );
                auto time_ms = std::chrono::duration_cast<std::chrono::milliseconds>(
                        now - sctp
                ).count();

                bool isExceedCacheTime = time_ms > static_cast<std::chrono::milliseconds::rep>(
                        max_cache_time
                );
                bool isShouldRetained =
                        keep_latest_file && (latest_file.empty() || sctp > latest_time);

                if (isExceedCacheTime) {
                    if (isShouldRetained) {
                        if (!latest_file.empty()) {
                            fs::remove(latest_file);
                            spdlog::log(
                                    spdlog::level::info,
                                    "{}  [{}] Deleted file: {}", "I",
                                    "LogFileDeleteCache",
                                    latest_file.string()
                            );
                        }
                        latest_file = entry.path();
                        latest_time = sctp;
                    } else {
                        fs::remove(entry.path());
                        spdlog::log(
                                spdlog::level::info,
                                "{}  [{}] Deleted file: {}", "I",
                                "LogFileDeleteCache",
                                entry.path().string()
                        );
                    }
                } else {
                    latest_file = entry.path();
                    latest_time = sctp;
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
    }
}


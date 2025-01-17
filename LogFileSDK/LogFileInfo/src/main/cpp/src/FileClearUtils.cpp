#include <iostream>
#include <filesystem>
#include "h/FileClearUtils.h"


namespace fs = std::__fs::filesystem;

void clean_old_logs(const std::string &log_dir, jlong max_cache_time) {
    auto now = std::chrono::system_clock::now();
    for (const auto &entry: fs::directory_iterator(log_dir)) {
        if (entry.is_regular_file()) {
            try {
                auto ftime = fs::last_write_time(entry);
                auto sctp = std::chrono::time_point_cast<std::chrono::system_clock::duration>(
                        ftime - decltype(ftime)::clock::now() + std::chrono::system_clock::now()
                );
                auto time_ms = std::chrono::duration_cast<std::chrono::milliseconds>(
                        now - sctp).count();
                if (time_ms > static_cast<std::chrono::milliseconds::rep>(max_cache_time)) {
                    fs::remove(entry.path());
                }
            } catch (const std::exception &e) {
                std::cerr << "Error processing file: " << entry.path() << " - " << e.what() << '\n';
            }
        }
    }
}

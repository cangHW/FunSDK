#include "../spdlog/include/spdlog/spdlog.h"
#include "spdlog/sinks/basic_file_sink.h"
#include "spdlog/sinks/rotating_file_sink.h"
#include "spdlog/sinks/daily_file_sink.h"

#include "h/LogFileInit.h"

void basic_logger(jboolean &isSync, const std::string &path) {
    if (isSync) {
        auto logger = spdlog::basic_logger_st("basic_logger", path);
        spdlog::set_default_logger(logger);
    } else {
        auto logger = spdlog::basic_logger_mt("basic_logger", path);
        spdlog::set_default_logger(logger);
    }
}

void rotating_logger(jboolean &isSync, const std::string &path, jlong &maxFileSize, jint &maxFiles) {
    if (isSync) {
        auto logger = spdlog::rotating_logger_st("rotating_logger", path, maxFileSize, maxFiles);
        spdlog::set_default_logger(logger);
    } else {
        auto logger = spdlog::rotating_logger_mt("rotating_logger", path, maxFileSize, maxFiles);
        spdlog::set_default_logger(logger);
    }
}

void daily_logger(jboolean &isSync, const std::string &path, jint &hour, jint &minute) {
    if (isSync) {
        auto logger = spdlog::daily_logger_st("daily_logger", path, hour, minute);
        spdlog::set_default_logger(logger);
    } else {
        auto logger = spdlog::daily_logger_mt("daily_logger", path, hour, minute);
        spdlog::set_default_logger(logger);
    }
}

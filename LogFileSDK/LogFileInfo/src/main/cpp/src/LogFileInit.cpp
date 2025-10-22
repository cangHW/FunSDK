#include "../spdlog/include/spdlog/spdlog.h"
#include "spdlog/sinks/basic_file_sink.h"
#include "spdlog/sinks/rotating_file_sink.h"
#include "spdlog/sinks/daily_file_sink.h"

#include "h/LogFileInit.h"
#include "h/security_sink.h"

std::shared_ptr<spdlog::logger> basic_logger(
        jboolean &isSync,
        const std::string &path,
        const std::string &compressionMode,
        const std::string &encryptionMode,
        const std::string &encryptionKey
) {
    if (isSync) {
        auto real_logger = spdlog::basic_logger_st(
                "basic_logger",
                path
        );
        auto logger = spdlog::enhanced_decorator_security_logger_st(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
        spdlog::set_default_logger(logger);
        return logger;
    } else {
        auto real_logger = spdlog::basic_logger_st(
                "basic_logger",
                path
        );
        auto logger = spdlog::enhanced_decorator_security_logger_mt(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
        spdlog::set_default_logger(logger);
        return logger;
    }
}

std::shared_ptr<spdlog::logger> rotating_logger(
        jboolean &isSync,
        const std::string &path,
        jlong &maxFileSize,
        jint &maxFiles,
        const std::string &compressionMode,
        const std::string &encryptionMode,
        const std::string &encryptionKey
) {
    if (isSync) {
        auto real_logger = spdlog::rotating_logger_st(
                "rotating_logger",
                path,
                maxFileSize,
                maxFiles
        );
        auto logger = spdlog::enhanced_decorator_security_logger_st(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
        spdlog::set_default_logger(logger);
        return logger;
    } else {
        auto real_logger = spdlog::rotating_logger_mt(
                "rotating_logger",
                path,
                maxFileSize,
                maxFiles
        );
        auto logger = spdlog::enhanced_decorator_security_logger_mt(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
        spdlog::set_default_logger(logger);
        return logger;
    }
}

std::shared_ptr<spdlog::logger> daily_logger(
        jboolean &isSync,
        const std::string &path,
        jint &hour,
        jint &minute,
        const std::string &compressionMode,
        const std::string &encryptionMode,
        const std::string &encryptionKey
) {
    if (isSync) {
        auto real_logger = spdlog::daily_logger_st(
                "daily_logger",
                path,
                hour,
                minute
        );
        auto logger = spdlog::enhanced_decorator_security_logger_st(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
        spdlog::set_default_logger(logger);
        return logger;
    } else {
        auto real_logger = spdlog::daily_logger_mt(
                "daily_logger",
                path,
                hour,
                minute
        );
        auto logger = spdlog::enhanced_decorator_security_logger_mt(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
        spdlog::set_default_logger(logger);
        return logger;
    }
}

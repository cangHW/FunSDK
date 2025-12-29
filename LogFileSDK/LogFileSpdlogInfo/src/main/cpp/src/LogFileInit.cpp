#include "../spdlog/include/spdlog/spdlog.h"
#include "spdlog/sinks/basic_file_sink.h"
#include "spdlog/sinks/rotating_file_sink.h"
#include "spdlog/sinks/daily_file_sink.h"

#include "h/LogFileInit.h"
#include "h/SecuritySink.h"

std::shared_ptr<spdlog::logger> basic_logger(
        jboolean &isSync,
        const std::string &path,
        jint &compressionMode,
        jint &encryptionMode,
        const std::string &encryptionKey
) {
    if (isSync) {
        auto real_logger = spdlog::basic_logger_st(
                "basic_logger",
                path
        );
        return spdlog::enhanced_decorator_security_logger_st(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    } else {
        auto real_logger = spdlog::basic_logger_st(
                "basic_logger",
                path
        );
        return spdlog::enhanced_decorator_security_logger_mt(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    }
}

std::shared_ptr<spdlog::logger> rotating_logger(
        jboolean &isSync,
        const std::string &path,
        jlong &maxFileSize,
        jint &maxFiles,
        jint &compressionMode,
        jint &encryptionMode,
        const std::string &encryptionKey
) {
    if (isSync) {
        auto real_logger = spdlog::rotating_logger_st(
                "rotating_logger",
                path,
                maxFileSize,
                maxFiles
        );
        return spdlog::enhanced_decorator_security_logger_st(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    } else {
        auto real_logger = spdlog::rotating_logger_mt(
                "rotating_logger",
                path,
                maxFileSize,
                maxFiles
        );
        return spdlog::enhanced_decorator_security_logger_mt(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    }
}

std::shared_ptr<spdlog::logger> daily_logger(
        jboolean &isSync,
        const std::string &path,
        jint &hour,
        jint &minute,
        jint &compressionMode,
        jint &encryptionMode,
        const std::string &encryptionKey
) {
    if (isSync) {
        auto real_logger = spdlog::daily_logger_st(
                "daily_logger",
                path,
                hour,
                minute
        );
        return spdlog::enhanced_decorator_security_logger_st(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    } else {
        auto real_logger = spdlog::daily_logger_mt(
                "daily_logger",
                path,
                hour,
                minute
        );
        return spdlog::enhanced_decorator_security_logger_mt(
                "security_logger",
                real_logger,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    }
}

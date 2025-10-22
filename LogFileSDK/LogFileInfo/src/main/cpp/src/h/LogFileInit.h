#include <jni.h>
#include <string>

#ifndef FUNSDK_LOGFILEINIT_H
#define FUNSDK_LOGFILEINIT_H

std::shared_ptr<spdlog::logger> basic_logger(
        jboolean &isSync,
        const std::string &path,
        const std::string &compressionMode,
        const std::string &encryptionMode,
        const std::string &encryptionKey
);

std::shared_ptr<spdlog::logger> rotating_logger(
        jboolean &isSync,
        const std::string &path,
        jlong &maxFileSize,
        jint &maxFiles,
        const std::string &compressionMode,
        const std::string &encryptionMode,
        const std::string &encryptionKey
);

std::shared_ptr<spdlog::logger> daily_logger(
        jboolean &isSync,
        const std::string &path,
        jint &hour,
        jint &minute,
        const std::string &compressionMode,
        const std::string &encryptionMode,
        const std::string &encryptionKey
);

#endif //FUNSDK_LOGFILEINIT_H

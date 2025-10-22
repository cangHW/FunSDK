#include <jni.h>
#include <string>

#ifndef FUNSDK_LOGFILEINIT_H
#define FUNSDK_LOGFILEINIT_H

std::shared_ptr<spdlog::logger> basic_logger(
        jboolean &isSync,
        const std::string &path,
        jboolean &isCompress,
        const std::string &cryptoKey
);

std::shared_ptr<spdlog::logger> rotating_logger(
        jboolean &isSync,
        const std::string &path,
        jlong &maxFileSize,
        jint &maxFiles,
        jboolean &isCompress,
        const std::string &cryptoKey
);

std::shared_ptr<spdlog::logger> daily_logger(
        jboolean &isSync,
        const std::string &path,
        jint &hour,
        jint &minute,
        jboolean &isCompress,
        const std::string &cryptoKey
);

#endif //FUNSDK_LOGFILEINIT_H

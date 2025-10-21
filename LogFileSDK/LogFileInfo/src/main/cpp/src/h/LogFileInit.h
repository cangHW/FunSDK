#include <jni.h>
#include <string>

#ifndef FUNSDK_LOGFILEINIT_H
#define FUNSDK_LOGFILEINIT_H

std::shared_ptr<spdlog::logger> basic_logger(jboolean &isSync, const std::string &path);

std::shared_ptr<spdlog::logger> rotating_logger(jboolean &isSync, const std::string &path, jlong &maxFileSize, jint &maxFiles);

std::shared_ptr<spdlog::logger> daily_logger(jboolean &isSync, const std::string &path, jint &hour, jint &minute);

#endif //FUNSDK_LOGFILEINIT_H

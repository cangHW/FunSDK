#include <jni.h>
#include <string>

#ifndef FUNSDK_LOGFILEINIT_H
#define FUNSDK_LOGFILEINIT_H

void basic_logger(jboolean &isSync, const std::string &path);

void rotating_logger(jboolean &isSync, const std::string &path, jlong &maxFileSize, jint &maxFiles);

void daily_logger(jboolean &isSync, const std::string &path, jint &hour, jint &minute);

#endif //FUNSDK_LOGFILEINIT_H

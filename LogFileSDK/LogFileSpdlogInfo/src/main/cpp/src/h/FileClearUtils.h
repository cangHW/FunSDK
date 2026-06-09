#include <jni.h>
#include <string>

#ifndef FUNSDK_FILECLEARUTILS_H
#define FUNSDK_FILECLEARUTILS_H

void clean_old_logs(const std::string &log_dir, jlong max_cache_time, jlong cleanTaskIntervalTime);
void stop_clean_task();

#endif //FUNSDK_FILECLEARUTILS_H

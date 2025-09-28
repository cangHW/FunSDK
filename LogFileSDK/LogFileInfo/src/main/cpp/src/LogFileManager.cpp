#include <jni.h>
#include <string>

#include "../spdlog/include/spdlog/spdlog.h"

#include "h/FileClearUtils.h"
#include "h/JniCallUtils.h"
#include "h/LogFileInit.h"

extern "C" JNIEXPORT jboolean JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_initTask(
        JNIEnv *env,
        jobject ojb,
        jobject config
) {
    std::string dir = callStringFrom(env, config, "getDir", "()Ljava/lang/String;");
    if (dir.empty()) {
        return false;
    }
    jlong cacheTime = callLongFrom(env, config, "getCacheTime", "()J");
    if (cacheTime <= 0) {
        return false;
    }
    jlong cleanTaskIntervalTime = callLongFrom(env, config, "getCleanTaskIntervalTime", "()J");
    if (cleanTaskIntervalTime <= 0) {
        return false;
    }
    clean_old_logs(dir, cacheTime, cleanTaskIntervalTime);

    std::string namePrefix = callStringFrom(env, config, "getNamePrefix", "()Ljava/lang/String;");
    if (namePrefix.empty()) {
        return false;
    }
    std::string namePostfix = callStringFrom(env, config, "getNamePostfix", "()Ljava/lang/String;");
    if (namePostfix.empty()) {
        return false;
    }
    std::string path = dir + namePrefix + namePostfix;

    jboolean isSync = callBooleanFrom(env, config, "isSyncMode", "()Z");
    jint type = callIntFrom(env, config, "getType", "()I");

    if (type == 0) {
        basic_logger(isSync, path);
    } else if (type == 1) {
        jlong maxFileSize = callLongFrom(env, config, "getSingleFileMaxSize", "()J");
        jint maxFiles = callIntFrom(env, config, "getMaxFileCount", "()I");

        rotating_logger(isSync, path, maxFileSize, maxFiles);
    } else if (type == 2) {
        jint hour = callIntFrom(env, config, "getHour", "()I");
        jint minute = callIntFrom(env, config, "getMinute", "()I");

        daily_logger(isSync, path, hour, minute);
    } else {
        return false;
    }

    int pid = getpid();
    std::string pkg = callStringFrom(env, config, "getPackageName", "()Ljava/lang/String;");
    if (!pkg.empty()) {
        spdlog::set_pattern("%v");
        spdlog::info(
                "\n\n\n---------------------------- PROCESS STARTED ({}) for package {} ----------------------------",
                pid, pkg);
    }

    spdlog::set_pattern("%Y-%m-%d %H:%M:%S.%e  " + std::to_string(pid) + "-%t  %v");
    return true;
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_log(
        JNIEnv *env,
        jobject job,
        jstring level,
        jstring tag,
        jstring msg
) {
    const char *levelString = (*env).GetStringUTFChars(level, nullptr);
    const char *tagString = (*env).GetStringUTFChars(tag, nullptr);
    const char *msgString = (*env).GetStringUTFChars(msg, nullptr);

    if (levelString != nullptr && tagString != nullptr && msgString != nullptr) {
        spdlog::log(spdlog::level::info, "{}  [{}] {}", levelString, tagString, msgString);
    }

    if (levelString != nullptr) {
        env->ReleaseStringUTFChars(level, levelString);
    }
    if (tagString != nullptr) {
        env->ReleaseStringUTFChars(tag, tagString);
    }
    if (msgString != nullptr) {
        env->ReleaseStringUTFChars(msg, msgString);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_flush(
        JNIEnv *env,
        jobject job
) {
    spdlog::default_logger()->flush();
}



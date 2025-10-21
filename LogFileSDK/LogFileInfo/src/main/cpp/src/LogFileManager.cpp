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

    std::shared_ptr<spdlog::logger> logger;

    if (type == 0) {
        logger = basic_logger(isSync, path);
    } else if (type == 1) {
        jlong maxFileSize = callLongFrom(env, config, "getSingleFileMaxSize", "()J");
        jint maxFiles = callIntFrom(env, config, "getMaxFileCount", "()I");

        logger = rotating_logger(isSync, path, maxFileSize, maxFiles);
    } else if (type == 2) {
        jint hour = callIntFrom(env, config, "getHour", "()I");
        jint minute = callIntFrom(env, config, "getMinute", "()I");

        logger = daily_logger(isSync, path, hour, minute);
    } else {
        return false;
    }

    int pid = getpid();
    std::string pkg = callStringFrom(env, config, "getPackageName", "()Ljava/lang/String;");
    if (!pkg.empty()) {
        logger->set_pattern("%v");
        logger->info(
                "\n\n\n---------------------------- PROCESS STARTED ({}) for package {} ----------------------------",
                pid, pkg);
    }

    logger->set_pattern("%Y-%m-%d %H:%M:%S.%e  " + std::to_string(pid) + "-%t  %v");
    logger->flush_on(spdlog::level::err);

    jlong flushEveryTime = callLongFrom(env, config, "getFlushEveryTime", "()J");
    if (cleanTaskIntervalTime != 0) {
        spdlog::flush_every(std::chrono::milliseconds(flushEveryTime));
    }
    return true;
}

std::shared_ptr<spdlog::logger> getLogger() {
    return spdlog::default_logger();
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_logV(
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
        getLogger()->log(spdlog::level::trace, "{}  [{}] {}", levelString, tagString, msgString);
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_logD(
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
        getLogger()->log(spdlog::level::debug, "{}  [{}] {}", levelString, tagString, msgString);
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_logI(
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
        getLogger()->log(spdlog::level::info, "{}  [{}] {}", levelString, tagString, msgString);
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_logW(
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
        getLogger()->log(spdlog::level::warn, "{}  [{}] {}", levelString, tagString, msgString);
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_logE(
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
        getLogger()->log(spdlog::level::err, "{}  [{}] {}", levelString, tagString, msgString);
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_logA(
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
        getLogger()->log(spdlog::level::critical, "{}  [{}] {}", levelString, tagString, msgString);
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_flush(
        JNIEnv *env,
        jobject job
) {
    getLogger()->flush();
}





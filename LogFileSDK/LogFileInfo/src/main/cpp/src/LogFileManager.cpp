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
    std::string dir = callStringFrom(env, config, "getDir");
    if (dir.empty()) {
        return false;
    }
    jlong cacheTime = callLongFrom(env, config, "getCacheTime");
    if (cacheTime <= 0) {
        return false;
    }
    jlong cleanTaskIntervalTime = callLongFrom(env, config, "getCleanTaskIntervalTime");
    if (cleanTaskIntervalTime <= 0) {
        return false;
    }
    clean_old_logs(dir, cacheTime, cleanTaskIntervalTime);

    std::string namePrefix = callStringFrom(env, config, "getNamePrefix");
    if (namePrefix.empty()) {
        return false;
    }
    std::string namePostfix = callStringFrom(env, config, "getNamePostfix");
    if (namePostfix.empty()) {
        return false;
    }
    std::string path = dir + namePrefix + namePostfix;

    jboolean isSync = callBooleanFrom(env, config, "isSyncMode");
    jint type = callIntFrom(env, config, "getType");
    jint compressionMode = callIntFrom(env, config, "getCompressionMode");
    jint encryptionMode = callIntFrom(env, config, "getEncryptionMode");
    std::string encryptionKey = callStringFrom(env, config, "getEncryptionKey");

    std::shared_ptr<spdlog::logger> logger;
    if (type == 0) {
        logger = basic_logger(
                isSync,
                path,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    } else if (type == 1) {
        jlong maxFileSize = callLongFrom(env, config, "getSingleFileMaxSize");
        jint maxFiles = callIntFrom(env, config, "getMaxFileCount");

        logger = rotating_logger(
                isSync,
                path,
                maxFileSize,
                maxFiles,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    } else if (type == 2) {
        jint hour = callIntFrom(env, config, "getHour");
        jint minute = callIntFrom(env, config, "getMinute");

        logger = daily_logger(
                isSync,
                path,
                hour,
                minute,
                compressionMode,
                encryptionMode,
                encryptionKey
        );
    } else {
        return false;
    }

    logger->set_level(spdlog::level::trace);
    spdlog::set_default_logger(logger);

    int pid = getpid();
    std::string pkg = callStringFrom(env, config, "getPackageName");
    if (!pkg.empty()) {
        logger->set_pattern("%v");
        logger->info(
                "\n\n\n---------------------------- PROCESS STARTED ({}) for package {} ----------------------------",
                pid,
                pkg
        );
    }
    logger->set_pattern("%Y-%m-%d %H:%M:%S.%e  " + std::to_string(pid) + "-%t  %v");
    logger->flush_on(spdlog::level::err);

    jlong flushEveryTime = callLongFrom(env, config, "getFlushEveryTime");
    if (cleanTaskIntervalTime != 0) {
        spdlog::flush_every(std::chrono::milliseconds(flushEveryTime));
    }
    return true;
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
        spdlog::default_logger()->log(
                spdlog::level::trace,
                "{}  [{}] {}",
                levelString,
                tagString,
                msgString
        );
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
        spdlog::default_logger()->log(
                spdlog::level::debug,
                "{}  [{}] {}",
                levelString,
                tagString,
                msgString
        );
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
        spdlog::default_logger()->log(
                spdlog::level::info,
                "{}  [{}] {}",
                levelString,
                tagString,
                msgString
        );
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
        spdlog::default_logger()->log(
                spdlog::level::warn,
                "{}  [{}] {}",
                levelString,
                tagString,
                msgString
        );
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
        spdlog::default_logger()->log(
                spdlog::level::err,
                "{}  [{}] {}",
                levelString,
                tagString,
                msgString
        );
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
        spdlog::default_logger()->log(
                spdlog::level::critical,
                "{}  [{}] {}",
                levelString,
                tagString,
                msgString
        );
    }

    releaseString(env, level, levelString, tag, tagString, msg, msgString);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileCore_flush(
        JNIEnv *env,
        jobject job
) {
    spdlog::default_logger()->flush();
}





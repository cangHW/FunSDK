#include "h/JniCallUtils.h"

std::string jStringToString(JNIEnv *env, jstring jStr) {
    if (!jStr) {
        return "";
    }
    const char *chars = env->GetStringUTFChars(jStr, nullptr);
    std::string str(chars);
    env->ReleaseStringUTFChars(jStr, chars);
    return str;
}

std::string callStringFrom(JNIEnv *env, jobject config, const char *name) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return "";
    }
    jmethodID method = env->GetMethodID(configClass, name, "()Ljava/lang/String;");
    if (method == nullptr) {
        return "";
    }
    auto obj = (jstring) env->CallObjectMethod(config, method);
    env->DeleteLocalRef(configClass);
    return jStringToString(env, obj);
}

jlong callLongFrom(JNIEnv *env, jobject config, const char *name) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return -1;
    }
    jmethodID method = env->GetMethodID(configClass, name, "()J");
    if (method == nullptr) {
        return -1;
    }
    jlong result = env->CallLongMethod(config, method);
    env->DeleteLocalRef(configClass);
    return result;
}

jint callIntFrom(JNIEnv *env, jobject config, const char *name) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return 0;
    }
    jmethodID method = env->GetMethodID(configClass, name, "()I");
    if (method == nullptr) {
        return 0;
    }
    jint result = env->CallIntMethod(config, method);
    env->DeleteLocalRef(configClass);
    return result;
}

jboolean callBooleanFrom(JNIEnv *env, jobject config, const char *name) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return 0;
    }
    jmethodID method = env->GetMethodID(configClass, name, "()Z");
    if (method == nullptr) {
        return 0;
    }
    jboolean result = env->CallBooleanMethod(config, method);
    env->DeleteLocalRef(configClass);
    return result;
}


void releaseString(
        JNIEnv *env,
        jstring level,
        const char *levelString,
        jstring tag,
        const char *tagString,
        jstring msg,
        const char *msgString
) {
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
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

std::string callStringFrom(JNIEnv *env, jobject config, const char *name, const char *sig) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return "";
    }
    jmethodID method = env->GetMethodID(configClass, name, sig);
    if (method == nullptr) {
        return "";
    }
    auto obj = (jstring) env->CallObjectMethod(config, method);
    env->DeleteLocalRef(configClass);
    return jStringToString(env, obj);
}

jlong callLongFrom(JNIEnv *env, jobject config, const char *name, const char *sig) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return -1;
    }
    jmethodID method = env->GetMethodID(configClass, name, sig);
    if (method == nullptr) {
        return -1;
    }
    jlong result = env->CallLongMethod(config, method);
    env->DeleteLocalRef(configClass);
    return result;
}

jint callIntFrom(JNIEnv *env, jobject config, const char *name, const char *sig) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return 0;
    }
    jmethodID method = env->GetMethodID(configClass, name, sig);
    if (method == nullptr) {
        return 0;
    }
    jint result = env->CallIntMethod(config, method);
    env->DeleteLocalRef(configClass);
    return result;
}

jboolean callBooleanFrom(JNIEnv *env, jobject config, const char *name, const char *sig) {
    jclass configClass = env->GetObjectClass(config);
    if (configClass == nullptr) {
        return 0;
    }
    jmethodID method = env->GetMethodID(configClass, name, sig);
    if (method == nullptr) {
        return 0;
    }
    jboolean result = env->CallBooleanMethod(config, method);
    env->DeleteLocalRef(configClass);
    return result;
}

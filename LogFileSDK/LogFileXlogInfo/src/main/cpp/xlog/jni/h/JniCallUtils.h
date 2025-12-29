#include <jni.h>
#include <string>

#ifndef FUNSDK_JNICALLUTILS_H
#define FUNSDK_JNICALLUTILS_H

std::string jStringToString(JNIEnv *env, jstring jStr);

std::string callStringFrom(JNIEnv *env, jobject config, const char *name);

jlong callLongFrom(JNIEnv *env, jobject config, const char *name);

jint callIntFrom(JNIEnv *env, jobject config, const char *name);

jboolean callBooleanFrom(JNIEnv *env, jobject config, const char *name);

void releaseString(
        JNIEnv *env,
        jstring level,
        const char *levelString,
        jstring tag,
        const char *tagString,
        jstring msg,
        const char *msgString
);

#endif //FUNSDK_JNICALLUTILS_H

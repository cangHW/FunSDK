#include <jni.h>
#include <string>

#ifndef FUNSDK_JNICALLUTILS_H
#define FUNSDK_JNICALLUTILS_H

std::string jStringToString(JNIEnv *env, jstring jStr);

std::string callStringFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

jlong callLongFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

jint callIntFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

jboolean callBooleanFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

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

#include <jni.h>
#include <string>

#ifndef FUNSDK_JNICALLUTILS_H
#define FUNSDK_JNICALLUTILS_H

std::string jStringToString(JNIEnv *env, jstring jStr);

std::string callStringFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

jlong callLongFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

jint callIntFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

jboolean callBooleanFrom(JNIEnv *env, jobject config, const char *name, const char *sig);

#endif //FUNSDK_JNICALLUTILS_H

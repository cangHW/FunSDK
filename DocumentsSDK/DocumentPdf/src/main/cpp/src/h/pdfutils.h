
#include <jni.h>

jobject NewLong(JNIEnv *env, jlong value);

jobject NewInteger(JNIEnv *env, jint value);

// JNI异常处理
void ThrowJavaException(JNIEnv *env, const char *className, const char *message);

int jniThrowException(JNIEnv *env, const char *className, const char *message);

int jniThrowExceptionFmt(JNIEnv *env, const char *className, const char *fmt, ...);

char *getErrorDescription(const long error);


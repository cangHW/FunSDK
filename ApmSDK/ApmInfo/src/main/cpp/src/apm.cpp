#include <jni.h>
#include <cstdlib>
#include <csignal>
#include "../native_crash/crash_handler.h"

#define CS_APM_EXPORT __attribute__((visibility("default")))

extern "C" {

CS_APM_EXPORT JNIEXPORT jint JNICALL
Java_com_proxy_service_apm_info_monitor_crash_native_1crash_jni_NativeCrashBridge_nativeInit(
        JNIEnv *env, jobject /* thiz */, jstring tombstone_dir) {
    const char *dir = env->GetStringUTFChars(tombstone_dir, NULL);
    if (dir == NULL) return -1;

    int ret = cs_apm_native_crash_init(dir);
    env->ReleaseStringUTFChars(tombstone_dir, dir);
    return ret;
}

CS_APM_EXPORT JNIEXPORT jint JNICALL
Java_com_proxy_service_apm_info_monitor_crash_native_1crash_jni_NativeCrashBridge_nativeDeinit(
        JNIEnv * /* env */, jobject /* thiz */) {
    return cs_apm_native_crash_deinit();
}

CS_APM_EXPORT JNIEXPORT void JNICALL
Java_com_proxy_service_apm_info_monitor_crash_native_1crash_jni_NativeCrashBridge_nativeTestCrash(
        JNIEnv * /* env */, jobject /* thiz */, jint type) {
    switch (type) {
        case 1:
            // SIGSEGV: 空指针解引用
            *((volatile int *) 0) = 0;
            break;
        case 2:
            // SIGABRT: 主动 abort
            abort();
            break;
        case 3: {
            // SIGSEGV: 非法地址写入
            volatile int *ptr = (volatile int *) 0xDEADBEEF;
            *ptr = 42;
            break;
        }
        default:
            // 默认：空指针
            *((volatile int *) 0) = 0;
            break;
    }
}

}

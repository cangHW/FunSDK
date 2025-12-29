// JNI_OnLoad.cpp
// xlog JNI 入口点

#include <jni.h>
#include <android/log.h>

// 声明 xlog 的 JNI 导出函数
extern void ExportXlog();

// 全局 JavaVM 指针
static JavaVM* g_jvm = nullptr;

JavaVM* GetJavaVM() {
    return g_jvm;
}

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    g_jvm = vm;
    
    JNIEnv* env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    
    __android_log_print(ANDROID_LOG_INFO, "xlog", "JNI_OnLoad called");
    
    // 导出 xlog 函数
    ExportXlog();
    
    return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    g_jvm = nullptr;
    __android_log_print(ANDROID_LOG_INFO, "xlog", "JNI_OnUnload called");
}



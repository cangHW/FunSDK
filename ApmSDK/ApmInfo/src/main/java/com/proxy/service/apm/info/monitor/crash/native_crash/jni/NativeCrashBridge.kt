package com.proxy.service.apm.info.monitor.crash.native_crash.jni

object NativeCrashBridge {

    init {
        System.loadLibrary("csApm")
    }

    external fun nativeInit(tombstoneDir: String): Int

    external fun nativeDeinit(): Int

    external fun nativeTestCrash(type: Int)

}

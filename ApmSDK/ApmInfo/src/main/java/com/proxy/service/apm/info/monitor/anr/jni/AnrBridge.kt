package com.proxy.service.apm.info.monitor.anr.jni

object AnrBridge {

    init {
        System.loadLibrary("csApm")
    }

    external fun nativeInit(markerDir: String): Int

    external fun nativeDeinit(): Int

    external fun nativeCheckAndReset(): Int

}

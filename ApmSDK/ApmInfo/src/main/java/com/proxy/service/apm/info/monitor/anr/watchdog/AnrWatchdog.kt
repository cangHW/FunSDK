package com.proxy.service.apm.info.monitor.anr.watchdog

import android.os.Handler
import android.os.Looper
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.anr.jni.AnrBridge
import com.proxy.service.core.framework.data.log.CsLogger

class AnrWatchdog(
    private val checkIntervalMs: Long,
    private val onAnrDetected: () -> Unit
) : Thread("${Constants.TAG}AnrWatchdog") {

    companion object {
        private const val TAG = "${Constants.TAG}AnrWatchdog"
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    @Volatile
    private var running = true

    override fun run() {
        while (running) {
            try {
                sleep(checkIntervalMs)
            } catch (e: InterruptedException) {
                break
            }

            if (!running) break

            // 检查 native 层是否收到了 SIGQUIT
            val detected = AnrBridge.nativeCheckAndReset()
            if (detected != 0) {
                CsLogger.tag(TAG).w("ANR signal detected (SIGQUIT)")
                onAnrDetected()
            }
        }
    }

    fun stopWatching() {
        running = false
        interrupt()
    }
}

package com.proxy.service.apm.info.monitor.anr.watchdog

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.anr.jni.AnrBridge
import com.proxy.service.core.framework.data.log.CsLogger

class AnrWatchdog(
    private val checkIntervalMs: Long,
    private val onAnrDetected: (signalCount: Int) -> Unit
) : Thread("${Constants.TAG}AnrWatchdog") {

    companion object {
        private const val TAG = "${Constants.TAG}AnrWatchdog"
    }

    @Volatile
    private var running = true

    override fun run() {
        while (running) {
            try {
                sleep(checkIntervalMs)
            } catch (e: InterruptedException) {
                break
            }

            if (!running) {
                break
            }

            val signalCount = AnrBridge.nativeCheckAndReset()
            if (signalCount > 0) {
                CsLogger.tag(TAG).w("ANR signal detected (SIGQUIT), count=$signalCount")
                onAnrDetected(signalCount)
            }
        }
    }

    fun stopWatching() {
        running = false
        interrupt()
    }
}

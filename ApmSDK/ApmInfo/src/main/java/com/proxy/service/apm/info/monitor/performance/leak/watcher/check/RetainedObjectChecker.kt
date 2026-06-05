package com.proxy.service.apm.info.monitor.performance.leak.watcher.check

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.watcher.reference.KeyedWeakReference

/**
 * 触发 GC 并确认对象是否仍被 retained。
 */
class RetainedObjectChecker {

    companion object {
        private const val TAG = "${Constants.TAG}MemoryLeakChecker"
    }

    fun confirmRetained(reference: KeyedWeakReference): RetainedObject? {
        repeat(Constants.MONITOR_MEMORY_LEAK_GC_REPEAT) {
            System.runFinalization()
            Runtime.getRuntime().gc()
            try {
                Thread.sleep(Constants.MONITOR_MEMORY_LEAK_GC_INTERVAL_MS)
            } catch (_: InterruptedException) {
                Thread.currentThread().interrupt()
                return null
            }
        }
        if (reference.get() == null) {
            reference.clearReference()
            return null
        }
        return RetainedObject(
            key = reference.key,
            description = reference.description,
            className = reference.key.substringBefore('@'),
            watchDurationMs = System.currentTimeMillis() - reference.watchTimeMs,
            reference
        )
    }
}

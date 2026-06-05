package com.proxy.service.apm.info.monitor.performance.leak.watcher.reference

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 * 带 key 的 WeakReference，dump 后可通过 Shark 在堆中定位。
 */
class KeyedWeakReference(
    referent: Any,
    referenceQueue: ReferenceQueue<Any>,
    val key: String,
    val description: String,
    val watchTimeMs: Long
) : WeakReference<Any>(referent, referenceQueue) {

    companion object {
        @JvmStatic
        val references: MutableSet<KeyedWeakReference> = mutableSetOf()
    }

    init {
        synchronized(references) {
            references.add(this)
        }
    }

    fun clearReference() {
        synchronized(references) {
            references.remove(this)
        }
        clear()
    }
}

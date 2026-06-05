package com.proxy.service.apm.info.monitor.performance.leak.watcher.check

import com.proxy.service.apm.info.monitor.performance.leak.watcher.reference.KeyedWeakReference

/**
 * @author: cangHX
 * @date: 2026/6/4 17:55
 * @desc: 已确认 retained 的对象摘要。
 */
data class RetainedObject(
    val key: String,
    val description: String,
    val className: String,
    val watchDurationMs: Long,
    val reference: KeyedWeakReference
)
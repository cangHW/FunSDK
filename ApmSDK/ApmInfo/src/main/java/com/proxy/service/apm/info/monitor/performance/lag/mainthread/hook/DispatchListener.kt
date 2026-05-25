package com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook

/**
 * Looper 单次 dispatch 起止回调，由 Hook 层驱动。
 *
 * @author: cangHX
 * @date: 2026/5/22 17:29
 */
interface DispatchListener {

    /**
     * @param hint 可选上下文（Observer 可能为空；Printer 为 AOSP 日志整行）
     */
    fun onDispatchStart(hint: String? = null)

    /**
     * @param hint Message 摘要或异常提示
     */
    fun onDispatchEnd(hint: String? = null)

}
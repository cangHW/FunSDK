package com.proxy.service.core.framework.data.log

/**
 * @author: cangHX
 * @data: 2024/4/28 19:18
 * @desc:
 */
interface LogCallback {

    fun onLog(priority: LogPriority, tag: String, message: String, t: Throwable?)

}
package com.proxy.service.apm.info.report.impl

import com.proxy.service.apm.info.report.IReporter

/**
 * @author: cangHX
 * @date: 2026/5/25 11:15
 * @desc:
 */
class CallbackReporter<T>(
    private val reporter: IReporter<T>?
) : IReporter<T> {
    override fun publish(time: Long, report: T) {
        reporter?.publish(time, report)
    }
}
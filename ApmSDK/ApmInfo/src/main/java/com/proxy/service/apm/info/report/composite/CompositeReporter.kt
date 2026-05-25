package com.proxy.service.apm.info.report.composite

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/22 18:30
 * @desc: 组合多个 Reporter（如落盘 + 业务回调等）。
 */
class CompositeReporter<T>(
    private vararg val reporters: IReporter<T>
) : IReporter<T> {

    companion object {
        private const val TAG = "${Constants.TAG}CompositeReporter"
    }

    override fun publish(time: Long, report: T) {
        reporters.forEach {
            try {
                it.publish(time, report)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }
}
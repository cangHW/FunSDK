package com.proxy.service.apm.info.report

/**
 * @author: cangHX
 * @date: 2026/5/22 18:29
 * @desc: 报告输出接口。
 */
interface IReporter<T> {

    /**
     * 发布报告。
     */
    fun publish(time: Long, report: T)

}
package com.proxy.service.apm.info.monitor.crash.java_crash.report

import com.proxy.service.apm.info.sampler.ICompositeSamplerData

/**
 * @author: cangHX
 * @date: 2026/5/25 16:22
 * @desc:
 */
class ExceptionReport(
    /**
     * 异常时间戳（System.currentTimeMillis）。
     */
    val time: Long,
    /**
     * 异常线程
     * */
    val thread: Thread,
    /**
     * 异常信息
     * */
    val throwable: Throwable,
    /**
     * 本次异常的采样数据。
     */
    val samplerData: List<ICompositeSamplerData>,
)
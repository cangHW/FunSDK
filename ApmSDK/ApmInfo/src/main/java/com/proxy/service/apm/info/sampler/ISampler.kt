package com.proxy.service.apm.info.sampler

/**
 * @author: cangHX
 * @date: 2026/5/22 15:39
 * @desc:
 */
interface ISampler<T> {

    /**
     * 启动采样
     * */
    fun start()

    /**
     * 停止采样
     * */
    fun stop()

    /**
     * 返回 [startMs, endMs] 窗口内的采样数据快照（用于组装报告）
     * */
    fun snapshotsInWindow(startMs: Long, endMs: Long): List<T>

    /**
     * 立即触发一次采样并返回采样数据
     */
    fun sampleNow(): List<T>
}
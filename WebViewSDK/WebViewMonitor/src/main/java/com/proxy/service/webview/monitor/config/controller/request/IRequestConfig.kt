package com.proxy.service.webview.monitor.config.controller.request

import com.proxy.service.webview.monitor.config.controller.base.IConfig
import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @date: 2026/6/8 14:12
 * @desc:
 */
interface IRequestConfig : IConfig<IRequestConfig> {

    /**
     * 设置重复请求预警配置
     *
     * @param minCount  最小数量, 预警阀值, 默认: [WebMonitorConstants.REQUEST_START_REPETITION_MIN_COUNT]
     * @param maxTime   最大时间 (ms), 预警阀值, 默认: [WebMonitorConstants.REQUEST_START_REPETITION_WINDOW_MS]
     * @param showToast 是否展示预警吐司, 默认: [WebMonitorConstants.REQUEST_START_ENABLE_TOAST]
     * */
    fun setRepetition(
        minCount: Int = WebMonitorConstants.REQUEST_START_REPETITION_MIN_COUNT,
        maxTime: Long = WebMonitorConstants.REQUEST_START_REPETITION_WINDOW_MS,
        showToast: Boolean = WebMonitorConstants.REQUEST_START_ENABLE_TOAST
    ): IRequestConfig

    /**
     * 设置并发请求预警配置
     *
     * @param minCount  最小数量, 预警阀值, 默认: [WebMonitorConstants.REQUEST_START_STORM_MIN_COUNT]
     * @param maxTime   最大时间 (ms), 预警阀值, 默认: [WebMonitorConstants.REQUEST_START_STORM_WINDOW_MS]
     * @param showToast 是否展示预警吐司, 默认: [WebMonitorConstants.REQUEST_START_ENABLE_TOAST]
     * */
    fun setStorm(
        minCount: Int = WebMonitorConstants.REQUEST_START_STORM_MIN_COUNT,
        maxTime: Long = WebMonitorConstants.REQUEST_START_STORM_WINDOW_MS,
        showToast: Boolean = WebMonitorConstants.REQUEST_START_ENABLE_TOAST
    ): IRequestConfig

    /**
     * 创建对象
     * */
    fun build(): RequestConfig
}
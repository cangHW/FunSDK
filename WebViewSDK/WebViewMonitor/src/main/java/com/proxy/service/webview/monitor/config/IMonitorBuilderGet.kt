package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.monitor.callback.MonitorCallback
import com.proxy.service.webview.monitor.callback.RequestMonitorCallback
import com.proxy.service.webview.monitor.config.controller.request.RequestConfig

/**
 * @author: cangHX
 * @date: 2026/1/23 12:55
 * @desc:
 */
interface IMonitorBuilderGet {

    /**
     * 是否打印 cookie
     * */
    fun isLogCookieEnable(): Boolean

    /**
     * cookie 回调
     * */
    fun getLogCookieCallback(): MonitorCallback?

    /**
     * 获取打印请求配置
     * */
    fun getLogRequestConfig(): RequestConfig

    /**
     * 请求回调
     * */
    fun getLogRequestCallback(): RequestMonitorCallback?

    /**
     * 是否打印页面加载耗时统计
     * */
    fun isLogLoadPageTimeEnable(): Boolean

    /**
     * 页面加载耗时统计回调
     * */
    fun getLogLoadPageTimeCallback(): MonitorCallback?

    /**
     * 是否打印页面资源加载耗时统计
     * */
    fun isLogLoadPageResourceTimeEnable(): Boolean

    /**
     * 页面资源加载耗时统计回调
     * */
    fun getLogLoadPageResourceTimeCallback(): MonitorCallback?

}
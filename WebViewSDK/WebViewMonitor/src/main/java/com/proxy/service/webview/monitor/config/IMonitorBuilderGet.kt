package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.monitor.callback.MonitorCallback

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
     * 是否打印请求
     * */
    fun isLogRequestEnable(): Boolean

    /**
     * 请求回调
     * */
    fun getLogRequestCallback(): MonitorCallback?

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
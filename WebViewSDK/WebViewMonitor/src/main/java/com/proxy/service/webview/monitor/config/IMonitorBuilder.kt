package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.base.web.callback.ValueCallback

/**
 * @author: cangHX
 * @data: 2026/1/23 12:55
 * @desc:
 */
interface IMonitorBuilder {

    /**
     * 是否打印 cookie, 默认 [WebMonitorConstants.ENABLE_LOG_COOKIE]
     *
     * @param callback 回调 web 页面使用的 cookie
     * */
    fun enableLogCookie(
        enable: Boolean,
        callback: ValueCallback<String>? = null
    ): IMonitorBuilder

    /**
     * 是否打印 Ajax 请求, 默认 [WebMonitorConstants.ENABLE_LOG_AJAX_REQUEST]
     *
     * @param callback 回调 web 页面的请求信息
     * */
    fun enableLogAjaxRequest(
        enable: Boolean,
        callback: ValueCallback<String>? = null
    ): IMonitorBuilder

    /**
     * 是否打印加载耗时统计, 默认 [WebMonitorConstants.ENABLE_LOG_LOAD_TIME]
     *
     * @param callback 回调 web 页面的加载耗时统计
     * */
    fun enableLogLoadTime(
        enable: Boolean,
        callback: ValueCallback<String>? = null
    ): IMonitorBuilder

    /**
     * 创建 WebMonitorConfig 对象
     * */
    fun build(): MonitorConfig
}
package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.base.web.callback.ValueCallback

/**
 * @author: cangHX
 * @data: 2026/1/23 12:55
 * @desc:
 */
interface IMonitorBuilder {

    /**
     * 是否打印 cookie
     *
     * @param callback 回调 web 页面使用的 cookie
     * */
    fun enableLogCookie(
        enable: Boolean,
        callback: ValueCallback<String>? = null
    ): IMonitorBuilder

    /**
     * 是否打印 Ajax 请求
     *
     * @param callback 回调 web 页面的请求信息
     * */
    fun enableLogAjaxRequest(
        enable: Boolean,
        callback: ValueCallback<String>? = null
    ): IMonitorBuilder

    /**
     * 创建 WebMonitorConfig 对象
     * */
    fun build(): MonitorConfig
}
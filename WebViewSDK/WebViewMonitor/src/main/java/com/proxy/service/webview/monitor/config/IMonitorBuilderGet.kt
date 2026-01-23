package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.base.web.callback.ValueCallback

/**
 * @author: cangHX
 * @data: 2026/1/23 12:55
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
    fun getLogCookieCallback(): ValueCallback<String>?

    /**
     * 是否打印 Ajax 请求
     * */
    fun isLogAjaxRequestEnable(): Boolean

    /**
     * Ajax 请求 回调
     * */
    fun getLogAjaxRequestCallback(): ValueCallback<String>?

}
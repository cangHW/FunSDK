package com.proxy.service.webview.base.web.request

import android.net.Uri

/**
 * @author: cangHX
 * @data: 2024/8/3 16:17
 * @desc:
 */
interface WebResourceRequest {

    /**
     * 获取请求的 url
     * */
    fun getUrl(): Uri

    /**
     * 判断这个请求是否是为主框架(main frame)发起的
     * */
    fun isForMainFrame(): Boolean

    /**
     * 判断这个请求是否是重定向
     * */
    fun isRedirect(): Boolean

    /**
     * 判断这个请求是否由用户手势触发
     * */
    fun hasGesture(): Boolean

    /**
     * 返回 HTTP 请求的方法，例如 "GET", "POST" 等
     * */
    fun getMethod(): String

    /**
     * 获取请求头
     * */
    fun getRequestHeaders(): Map<String, String>

}
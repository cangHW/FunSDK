package com.proxy.service.webview.base.web.request

import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2024/8/3 17:49
 * @desc:
 */
interface WebResourceResponse {

    /**
     * 获取内容类型（媒体类型），例如 "text/html", "image/png" 等
     * */
    fun getMimeType(): String

    /**
     * 获取内容编码类型，例如 "UTF-8"
     * */
    fun getEncoding(): String

    /**
     * 获取 HTTP 状态码
     * */
    fun getStatusCode(): Int

    /**
     * 获取状态码的原因短语
     * */
    fun getReasonPhrase(): String

    /**
     * 获取响应头的键值对集合
     * */
    fun getResponseHeaders(): Map<String, String>

    /**
     * 获取响应数据的输入流
     * */
    fun getData(): InputStream?

}
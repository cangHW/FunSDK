package com.proxy.service.webview.base.web.error

import android.net.http.SslCertificate

/**
 * @author: cangHX
 * @data: 2024/8/3 10:48
 * @desc:
 */
interface SslError {

    /**
     * 获取导致 SSL 错误的证书对象
     * */
    fun getCertificate(): SslCertificate

    /**
     * 将指定的错误码添加到当前 SSL 错误中
     * */
    fun addError(error: Int): Boolean

    /**
     * 检查是否存在特定的 SSL 错误码
     * */
    fun hasError(error: Int): Boolean

    /**
     * 获取主要的 SSL 错误码
     * */
    fun getPrimaryError(): Int

    /**
     * 获取触发 SSL 错误的 URL 地址
     * */
    fun getUrl(): String

}
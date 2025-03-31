package com.proxy.service.apihttp.base.common.config.safety

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @data: 2025/3/27 20:04
 * @desc:
 */
interface ISafetyBuilder<T> {

    /**
     * 设置 ssl 证书校验为服务端单向校验
     * */
    fun setSslSocket(serverCerAssetsName: String): T

    /**
     * 设置 ssl 证书校验为客户端、服务端双向校验
     * */
    fun setSslSocket(
        serverCerAssetsName: String,
        clientCerAssetsName: String,
        clientCerPassWord: String,
    ): T

    /**
     * 设置证书管理器
     * */
    fun setX509TrustManager(x509TrustManager: X509TrustManager): T

    /**
     * 设置域名校验器
     * */
    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier): T
}
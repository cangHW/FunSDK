package com.proxy.service.apihttp.base.common.config.safety

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @data: 2025/3/27 20:04
 * @desc:
 */
interface ISafetyBuilderGet {

    /**
     * 获取服务端证书路径
     * */
    fun getServerCerAssetsName(): String?

    /**
     * 获取客户端证书路径
     * */
    fun getClientCerAssetsName(): String?

    /**
     * 获取客户端证书密钥
     * */
    fun getClientCerPassWord(): String?

    /**
     * 获取证书管理器
     * */
    fun getX509TrustManager(): X509TrustManager?

    /**
     * 获取域名校验器
     * */
    fun getHostnameVerifier(): HostnameVerifier?

}
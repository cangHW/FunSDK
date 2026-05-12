package com.proxy.service.apihttp.base.common.config.safety

import com.proxy.service.apihttp.base.constants.ApiConstants
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @date: 2025/3/27 20:04
 * @desc:
 */
interface ISafetyBuilderGet {

    /**
     * http 重定向是否允许
     * */
    fun getFollowRedirects(): Boolean

    /**
     * https 重定向是否允许
     * */
    fun getFollowSslRedirects(): Boolean

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
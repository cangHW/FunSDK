package com.proxy.service.apihttp.base.download.config.builder

import com.proxy.service.apihttp.base.download.config.DownloadGroup
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IDownloadConfigBuilderGet {

    /**
     * 获取组信息
     * */
    fun getGroups(): ArrayList<DownloadGroup>

    /**
     * 获取最大同时下载任务数量
     * */
    fun getMaxTask(): Int

    /**
     * 获取是否允许网络连接恢复时自动重新启动失败的下载任务
     * */
    fun getAutoRestartOnNetworkReconnect(): Boolean

    /**
     * 获取是否允许应用重启时自动恢复未完成的下载任务
     * */
    fun getAutoResumeOnAppRelaunch(): Boolean

    /**
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long

    /**
     * 获取应用级别拦截器
     * */
    fun getInterceptor(): MutableList<Interceptor>

    /**
     * 获取网络级别拦截器
     * */
    fun getNetworkInterceptor(): MutableList<Interceptor>

    /**
     * 获取事件节点回调
     * */
    fun getEventListener(): EventListener?

    /**
     * 获取 dns 拦截
     * */
    fun getDns(): Dns?

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

}
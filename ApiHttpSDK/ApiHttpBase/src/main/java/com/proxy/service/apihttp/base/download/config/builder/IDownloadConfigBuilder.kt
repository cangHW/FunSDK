package com.proxy.service.apihttp.base.download.config.builder

import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IDownloadConfigBuilder {

    /**
     * 添加组信息
     * */
    fun addGroup(group: DownloadGroup): IDownloadConfigBuilder

    /**
     * 设置最大同时下载任务数量, 默认为：3
     * */
    fun setMaxTask(maxTasks: Int = 3): IDownloadConfigBuilder

    /**
     * 设置是否允许网络连接恢复时自动重新启动失败的下载任务
     * */
    fun setAutoRestartOnNetworkReconnect(enable: Boolean): IDownloadConfigBuilder

    /**
     * 设置是否允许应用重启时自动恢复未完成的下载任务
     * */
    fun setAutoResumeOnAppRelaunch(enable: Boolean): IDownloadConfigBuilder

    /**
     * 设置连接超时时间
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IDownloadConfigBuilder

    /**
     * 添加应用级别拦截器
     * */
    fun addInterceptor(interceptor: Interceptor): IDownloadConfigBuilder

    /**
     * 添加网络级别拦截器
     * */
    fun addNetworkInterceptor(interceptor: Interceptor): IDownloadConfigBuilder

    /**
     * 设置事件节点回调
     * */
    fun setEventListener(eventListener: EventListener): IDownloadConfigBuilder

    /**
     * 设置 dns 拦截
     * */
    fun setDns(dns: Dns): IDownloadConfigBuilder

    /**
     * 设置 ssl 证书校验为服务端单向校验
     * */
    fun setSslSocket(serverCerAssetsName: String): IDownloadConfigBuilder

    /**
     * 设置 ssl 证书校验为客户端、服务端双向校验
     * */
    fun setSslSocket(
        serverCerAssetsName: String,
        clientCerAssetsName: String,
        clientCerPassWord: String
    ): IDownloadConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): DownloadConfig

}
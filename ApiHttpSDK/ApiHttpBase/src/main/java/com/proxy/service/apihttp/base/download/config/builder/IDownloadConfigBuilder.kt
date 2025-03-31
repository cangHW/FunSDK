package com.proxy.service.apihttp.base.download.config.builder

import com.proxy.service.apihttp.base.common.config.common.ICommonBuilder
import com.proxy.service.apihttp.base.common.config.safety.ISafetyBuilder
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IDownloadConfigBuilder : ISafetyBuilder<IDownloadConfigBuilder>,
    ICommonBuilder<IDownloadConfigBuilder> {

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
     * 设置连接超时时间
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IDownloadConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): DownloadConfig

}
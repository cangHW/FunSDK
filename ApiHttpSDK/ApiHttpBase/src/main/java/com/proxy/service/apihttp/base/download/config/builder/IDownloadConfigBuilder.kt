package com.proxy.service.apihttp.base.download.config.builder

import androidx.annotation.IntRange
import com.proxy.service.apihttp.base.common.config.base.IBaseConfig
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import java.util.concurrent.TimeUnit
import com.proxy.service.apihttp.base.constants.ApiConstants

/**
 * @author: cangHX
 * @date: 2024/5/21 20:43
 * @desc:
 */
interface IDownloadConfigBuilder : IBaseConfig<IDownloadConfigBuilder> {

    /**
     * 添加组信息
     * */
    fun addGroup(group: DownloadGroup): IDownloadConfigBuilder

    /**
     * 设置最大同时下载任务数量, 默认为 [ApiConstants.Download.MAX_TASK_NUM]
     * */
    fun setMaxTaskCount(@IntRange(from = 1, to = 5) maxTaskNum: Int): IDownloadConfigBuilder

    /**
     * 设置是否允许网络连接恢复时自动重新启动失败的下载任务
     * */
    fun setAutoRestartOnNetworkReconnect(enable: Boolean): IDownloadConfigBuilder

    /**
     * 设置连接超时时间. 最小时间为 [ApiConstants.DEFAULT_TIMEOUT_MIN]
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IDownloadConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): DownloadConfig

}
package com.proxy.service.apihttp.base.download.config.base

import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.config.DownloadGroup

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IConfigBuilder {

    /**
     * 添加组信息
     * */
    fun addGroup(group: DownloadGroup): IConfigBuilder

    /**
     * 设置最大同时下载任务数量, 默认为：3
     * */
    fun setMaxTask(maxTask: Int = 3): IConfigBuilder

    /**
     * 设置是否允许网络连接恢复时自动重新启动失败的下载任务
     * */
    fun setAutoRestartOnNetworkReconnect(enable: Boolean): IConfigBuilder

    /**
     * 设置是否允许应用重启时自动恢复未完成的下载任务
     * */
    fun setAutoResumeOnAppRelaunch(enable: Boolean): IConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): DownloadConfig

}
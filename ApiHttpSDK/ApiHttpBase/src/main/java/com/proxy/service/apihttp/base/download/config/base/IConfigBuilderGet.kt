package com.proxy.service.apihttp.base.download.config.base

import com.proxy.service.apihttp.base.download.config.DownloadGroup

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IConfigBuilderGet {

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
}
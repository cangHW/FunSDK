package com.proxy.service.apihttp.base.download.config.builder

import com.proxy.service.apihttp.base.common.config.base.IBaseConfigGet
import com.proxy.service.apihttp.base.download.config.DownloadGroup

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IDownloadConfigBuilderGet : IBaseConfigGet {

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
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long

}
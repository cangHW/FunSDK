package com.proxy.service.apihttp.info.config

import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.request.config.RequestConfig

/**
 * @author: cangHX
 * @data: 2024/5/21 18:09
 * @desc:
 */
object Config {

    @Volatile
    private var isDebug = true

    /*********  request  *********/
    /**
     * 接口请求配置
     * */
    @Volatile
    private var requestConfig: RequestConfig? = null

    /*********  download  *********/
    /**
     * 最大同时下载数量
     * */
    @Volatile
    private var maxTask: Int = 3


    fun setIsDebug(isDebug: Boolean) {
        Config.isDebug = isDebug
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    /*********  request  *********/
    fun setRequestConfig(config: RequestConfig) {
        requestConfig = config
    }

    fun getRequestConfig(): RequestConfig {
        return requestConfig ?: RequestConfig.builder("").build()
    }

    /*********  download  *********/
    fun setMaxDownloadTask(maxTask: Int) {
        this.maxTask = maxTask
    }

    fun getMaxDownloadTask(): Int {
        return maxTask
    }
}
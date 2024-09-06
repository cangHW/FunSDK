package com.proxy.service.apihttp.info.config

import com.proxy.service.apihttp.base.init.ApiConfig

/**
 * @author: cangHX
 * @data: 2024/5/21 18:09
 * @desc:
 */
object Config {

    const val LOG_TAG_START = "ApiHttp_"

    private var isDebug = true
    private var config: ApiConfig? = null

    fun setIsDebug(isDebug: Boolean) {
        Config.isDebug = isDebug
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    fun setApiConfig(config: ApiConfig) {
        Config.config = config
    }

    fun getApiConfig(): ApiConfig {
        return config ?: ApiConfig.builder("").build()
    }
}
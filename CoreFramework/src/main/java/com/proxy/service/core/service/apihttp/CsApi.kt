package com.proxy.service.core.service.apihttp

import com.proxy.service.api.CloudSystem
import com.proxy.service.apihttp.base.request.RequestService
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * http 网络框架入口
 *
 * @author: cangHX
 * @data: 2024/7/18 16:42
 * @desc:
 */
object CsApi {
    private const val TAG = "${CoreConfig.TAG}Api"

    private var service: RequestService? = null

    private fun getService(): RequestService? {
        if (service == null) {
            service = CloudSystem.getService(RequestService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG).e("Please check to see if it is referenced. <io.github.canghw:Service-Apihttp:xxx>")
        }
        return service
    }

    private var config: RequestConfig? = null

    /**
     * 网络库初始化
     * */
    fun init(config: RequestConfig) {
        CsApi.config = config
    }

    /**
     * 获取请求接口
     * */
    fun <T> getService(service: Class<T>): T? {
        config?.let {
            val request = getService()
            request?.init(it)
            return request?.getService(service)
        } ?: let {
            CsLogger.tag(TAG).e("Please check whether initialization has been performed.")
        }
        return null
    }
}
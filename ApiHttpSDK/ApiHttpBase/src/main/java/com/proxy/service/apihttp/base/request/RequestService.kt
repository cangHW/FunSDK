package com.proxy.service.apihttp.base.request

import com.proxy.service.base.BaseService
import com.proxy.service.apihttp.base.request.config.RequestConfig
import com.proxy.service.apihttp.base.request.annotation.CsBaseUrlKey

/**
 * @author: cangHX
 * @date: 2024/5/21 10:26
 * @desc:
 */
interface RequestService : BaseService {

    /**
     * 初始化
     * */
    fun init(config: RequestConfig)

    /**
     * 添加全局 base url. 搭配注解使用[CsBaseUrlKey]
     * */
    fun addGlobalBaseUrl(baseUrlKey: String, baseUrl: String)

    /**
     * 获取请求接口
     * */
    fun <T> getService(service: Class<T>): T

}
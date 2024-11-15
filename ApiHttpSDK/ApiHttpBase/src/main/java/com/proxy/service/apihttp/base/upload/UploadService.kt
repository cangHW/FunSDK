package com.proxy.service.apihttp.base.upload

import com.proxy.service.base.BaseService
import com.proxy.service.apihttp.base.request.config.RequestConfig

/**
 * @author: cangHX
 * @data: 2024/5/21 10:26
 * @desc:
 */
interface UploadService : BaseService {

    /**
     * 网络库初始化
     * */
    fun init(config: RequestConfig)


    /**
     * 获取请求接口
     * */
    fun <T> getService(service: Class<T>): T

}
package com.proxy.service.apihttp.base

import com.proxy.service.base.BaseService
import com.proxy.service.apihttp.base.init.ApiConfig

/**
 * @author: cangHX
 * @data: 2024/5/21 10:26
 * @desc:
 */
interface ApiHttpService : BaseService {

    /**
     * 网络库初始化
     * */
    fun init(config: ApiConfig)


    /**
     * 获取请求接口
     * */
    fun <T> getService(service: Class<T>): T

}
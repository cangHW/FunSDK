package com.proxy.service.apihttp.base.common.config.common

import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2025/3/27 20:04
 * @desc:
 */
interface ICommonBuilder<T> {

    /**
     * 添加应用级别拦截器
     * */
    fun addInterceptor(interceptor: Interceptor): T

    /**
     * 添加网络级别拦截器
     * */
    fun addNetworkInterceptor(interceptor: Interceptor): T

    /**
     * 设置事件节点回调
     * */
    fun setEventListener(eventListener: EventListener): T

    /**
     * 设置 dns 拦截
     * */
    fun setDns(dns: Dns): T

}
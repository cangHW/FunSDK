package com.proxy.service.apihttp.base.common.config.common

import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor

/**
 * @author: cangHX
 * @data: 2025/3/27 20:04
 * @desc:
 */
interface ICommonBuilderGet {

    /**
     * 获取应用级别拦截器
     * */
    fun getInterceptor(): MutableList<Interceptor>

    /**
     * 获取网络级别拦截器
     * */
    fun getNetworkInterceptor(): MutableList<Interceptor>

    /**
     * 获取事件节点回调
     * */
    fun getEventListener(): EventListener?

    /**
     * 获取 dns 拦截
     * */
    fun getDns(): Dns?
}
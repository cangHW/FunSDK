package com.proxy.service.apihttp.base.common.config.base

/**
 * @author: cangHX
 * @date: 2025/3/27 20:28
 * @desc:
 */
interface IBase<T> {

    /**
     * 获取当前对象
     * */
    fun getInstance(): T

    /**
     * 复制
     * */
    fun copyFrom(any: Any)
}
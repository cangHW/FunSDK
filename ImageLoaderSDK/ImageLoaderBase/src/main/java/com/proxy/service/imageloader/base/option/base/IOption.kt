package com.proxy.service.imageloader.base.option.base

/**
 * @author: cangHX
 * @data: 2025/9/13 14:10
 * @desc:
 */
interface IOption<T> {

    /**
     * 设置动画加载异常回调
     * */
    fun setLoadErrorCallback(callback: LoadErrorCallback): T

}
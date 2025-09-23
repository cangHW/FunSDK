package com.proxy.service.core.framework.collections.callback

/**
 * @author: cangHX
 * @data: 2025/9/17 15:31
 * @desc:
 */
abstract class OnDataChangedCallback<T> {

    /**
     * 数据添加回调
     * */
    open fun onDataAdd(t: T) {}

    /**
     * 数据移除回调
     * */
    open fun onDataRemoved(t: T) {}

}
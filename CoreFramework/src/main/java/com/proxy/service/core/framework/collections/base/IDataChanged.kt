package com.proxy.service.core.framework.collections.base

import com.proxy.service.core.framework.collections.callback.OnDataChangedCallback

/**
 * @author: cangHX
 * @data: 2025/9/17 15:37
 * @desc:
 */
interface IDataChanged<V> {

    /**
     * 添加数据变化监听
     * */
    fun addDataChangedCallback(callback: OnDataChangedCallback<V>)

    /**
     * 移除数据变化监听
     * */
    fun removeDataChangedCallback(callback: OnDataChangedCallback<V>)

}
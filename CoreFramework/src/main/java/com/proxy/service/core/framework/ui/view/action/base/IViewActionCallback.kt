package com.proxy.service.core.framework.ui.view.action.base

/**
 * @author: cangHX
 * @data: 2026/1/8 17:10
 * @desc:
 */
fun interface IViewActionCallback<T> {

    /**
     * view 事件回调
     * */
    fun onViewActionCall(t: T)

}
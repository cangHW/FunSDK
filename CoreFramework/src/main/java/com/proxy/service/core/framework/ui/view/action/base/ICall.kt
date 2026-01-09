package com.proxy.service.core.framework.ui.view.action.base

/**
 * @author: cangHX
 * @data: 2026/1/9 14:18
 * @desc:
 */
interface ICall<T, R> {

    /**
     * 配置回调
     * */
    fun call(callback: IViewActionCallback<T>): R

}
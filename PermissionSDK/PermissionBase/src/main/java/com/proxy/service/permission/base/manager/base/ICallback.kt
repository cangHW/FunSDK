package com.proxy.service.permission.base.manager.base

import com.proxy.service.permission.base.callback.ActionCallback

/**
 * @author: cangHX
 * @data: 2024/11/20 20:49
 * @desc:
 */
interface ICallback<T> {

    /**
     * 允许的权限回调
     * */
    fun setGrantedCallback(callback: ActionCallback): T

    /**
     * 拒绝的权限回调
     * */
    fun setDeniedCallback(callback: ActionCallback): T

    /**
     * 拒绝并不再提示的权限回调
     * */
    fun setNoPromptCallback(callback: ActionCallback): T

}
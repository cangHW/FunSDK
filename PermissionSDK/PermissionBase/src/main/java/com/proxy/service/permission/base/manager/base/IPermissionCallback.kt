package com.proxy.service.permission.base.manager.base

import com.proxy.service.permission.base.callback.ActionCallback

/**
 * @author: cangHX
 * @data: 2024/11/20 20:49
 * @desc:
 */
interface IPermissionCallback<T> {

    /**
     * 添加要申请的权限
     * */
    fun addPermission(permission: String): T

    /**
     * 允许的权限回调
     * */
    fun setGrantedCallback(callback: ActionCallback): T

    /**
     * 拒绝的权限回调
     * */
    fun setDeniedCallback(callback: ActionCallback): T

}
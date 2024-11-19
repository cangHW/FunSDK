package com.proxy.service.permission.base.manager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.permission.base.callback.DeniedActionCallback
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.NoPromptActionCallback

/**
 * 请求权限
 *
 * @author: cangHX
 * @data: 2024/11/18 10:22
 * @desc:
 */
interface IPermissionRequest {

    /**
     * 添加要申请的权限
     * */
    fun addPermission(permission: String): IPermissionRequest

    /**
     * 允许的权限回调
     * */
    fun setGrantedCallback(callback: ActionCallback): IPermissionRequest

    /**
     * 拒绝的权限回调
     * */
    fun setDeniedCallback(callback: DeniedActionCallback): IPermissionRequest

    /**
     * 拒绝并不再提示的权限回调
     * */
    fun setNoPromptCallback(callback: NoPromptActionCallback): IPermissionRequest

    /**
     * 开始申请
     * */
    fun start(activity: FragmentActivity)

    /**
     * 开始申请
     * */
    fun start(fragment: Fragment)

    /**
     * 开始申请
     * */
    fun start()
}
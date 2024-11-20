package com.proxy.service.permission.info.fragment

import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.info.PermissionServiceImpl
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/11/18 18:12
 * @desc:
 */
interface IRequest {

    /**
     * 添加要申请的权限
     * */
    fun addPermission(permission: String)

    /**
     * 允许的权限回调
     * */
    fun setGrantedCallback(callback: ActionCallback)

    /**
     * 拒绝的权限回调
     * */
    fun setDeniedCallback(callback: ActionCallback)

    /**
     * 拒绝并不再提示的权限回调
     * */
    fun setNoPromptCallback(callback: ActionCallback)

    /**
     * 开始申请权限
     */
    fun request()

}
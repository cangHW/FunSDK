package com.proxy.service.permission.base.manager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.permission.base.manager.base.ICallback

/**
 * 请求权限
 *
 * @author: cangHX
 * @data: 2024/11/18 10:22
 * @desc:
 */
interface IPermissionRequest : ICallback<IPermissionRequest> {

    /**
     * 添加要申请的权限
     * */
    fun addPermission(permission: String): IPermissionRequest

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
package com.proxy.service.permission.info.fragment

import com.proxy.service.permission.base.manager.base.IPermissionCallback

/**
 * @author: cangHX
 * @data: 2024/11/18 18:12
 * @desc:
 */
interface ISetting : IPermissionCallback<Unit> {

    /**
     * 添加要申请的权限
     * */
    fun setPermission(permission: Array<String>)

    /**
     * 开始申请权限
     */
    fun request()

}
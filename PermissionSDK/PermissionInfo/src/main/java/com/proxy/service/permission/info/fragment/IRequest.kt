package com.proxy.service.permission.info.fragment

import com.proxy.service.permission.base.manager.base.ICallback

/**
 * @author: cangHX
 * @data: 2024/11/18 18:12
 * @desc:
 */
interface IRequest:ICallback<Unit> {

    /**
     * 添加要申请的权限
     * */
    fun addPermission(permission: String)

    /**
     * 开始申请权限
     */
    fun request()

}
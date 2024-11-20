package com.proxy.service.permission.base

import com.proxy.service.base.BaseService
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.IPermissionRequest

/**
 * @author: cangHX
 * @data: 2024/11/18 10:02
 * @desc:
 */
interface PermissionService : BaseService {

    /**
     * 是否具有对应权限
     * */
    fun isPermissionGranted(permission: String): Boolean

    /**
     * 设置弹窗工厂
     * */
    fun setDialogFactory(factory: DialogFactory)

    /**
     * 创建权限请求器, 用于请求权限
     * */
    fun createRequest(): IPermissionRequest
}
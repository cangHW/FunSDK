package com.proxy.service.permission.base

import com.proxy.service.base.BaseService
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.IPermissionRequest
import com.proxy.service.permission.base.manager.base.IRationaleDialog
import com.proxy.service.permission.base.manager.base.ISettingDialog

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
     * 创建权限请求器, 用于请求权限, 最好配置旋转屏幕时不重建当前页面, 避免横屏页面跳转竖屏设置后返回等问题。
     * */
    fun createRequest(): IPermissionRequest

    /**
     * 创建一个用于展示所需权限理由的弹窗, 最好配置旋转屏幕时不重建当前页面, 避免横屏页面跳转竖屏设置后返回等问题。
     * */
    fun createRationaleDialog(): IRationaleDialog

    /**
     * 创建一个用于展示跳转 setting 的弹窗, 最好配置旋转屏幕时不重建当前页面, 避免横屏页面跳转竖屏设置后返回等问题。
     * */
    fun createSettingDialog(): ISettingDialog

}
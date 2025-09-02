package com.proxy.service.core.service.permission

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.PermissionService
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.IPermissionRequest
import com.proxy.service.permission.base.manager.base.IRationaleDialog
import com.proxy.service.permission.base.manager.base.ISettingDialog

/**
 * 权限框架入口
 *
 * @author: cangHX
 * @data: 2024/11/18 15:54
 * @desc:
 */
object CsPermission {

    private const val TAG = "${CoreConfig.TAG}Permission"

    private var service: PermissionService? = null

    private fun getService(): PermissionService? {
        if (service == null) {
            service = CloudSystem.getService(PermissionService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.canghw:Service-Permission:xxx>")
        }
        return service
    }

    /**
     * 是否具有对应权限
     * */
    fun isPermissionGranted(permission: String): Boolean {
        return getService()?.isPermissionGranted(permission) ?: false
    }

    /**
     * 设置弹窗工厂
     * */
    fun setDialogFactory(factory: DialogFactory) {
        getService()?.setDialogFactory(factory)
    }

    /**
     * 创建权限请求器, 用于请求权限
     * */
    fun createRequest(): IPermissionRequest? {
        return getService()?.createRequest()
    }

    /**
     * 创建一个用于展示所需权限理由的弹窗
     * */
    fun createRationaleDialog(): IRationaleDialog? {
        return getService()?.createRationaleDialog()
    }

    /**
     * 创建一个用于展示跳转 setting 的弹窗
     * */
    fun createSettingDialog(): ISettingDialog? {
        return getService()?.createSettingDialog()
    }

}
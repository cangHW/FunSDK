package com.proxy.service.core.service.permission

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.PermissionService
import com.proxy.service.permission.base.manager.IPermissionRequest

/**
 * 权限框架入口
 *
 * @author: cangHX
 * @data: 2024/11/18 15:54
 * @desc:
 */
object CsPermission {

    private const val TAG = "${Constants.TAG}Permission"

    private var service: PermissionService? = null

    private fun getService(): PermissionService? {
        if (service == null) {
            service = CloudSystem.getService(PermissionService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.cangHW:Service-Permission:xxx>")
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
     * 创建权限请求器, 用于请求权限
     * */
    fun createRequest(): IPermissionRequest? {
        return getService()?.createRequest()
    }

}
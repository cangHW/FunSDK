package com.proxy.service.permission.info

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.permission.base.PermissionService
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.IPermissionRequest
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.request.PermissionRequestImpl


/**
 * @author: cangHX
 * @data: 2024/11/18 10:30
 * @desc:
 */
@CloudApiService(serviceTag = "service/permission")
class PermissionServiceImpl: PermissionService {
    /**
     * 是否具有对应权限
     * */
    override fun isPermissionGranted(permission: String): Boolean {
        val context = CsContextManager.getApplication()
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 设置弹窗工厂
     * */
    override fun setDialogFactory(factory: DialogFactory) {
        Config.setDialogFactory(factory)
    }

    /**
     * 创建权限请求器, 用于请求权限
     * */
    override fun createRequest(): IPermissionRequest {
        return PermissionRequestImpl()
    }
}
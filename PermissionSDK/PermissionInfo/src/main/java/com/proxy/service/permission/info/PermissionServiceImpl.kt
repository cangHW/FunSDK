package com.proxy.service.permission.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.PermissionService
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.IPermissionRequest
import com.proxy.service.permission.base.manager.base.IRationaleDialog
import com.proxy.service.permission.base.manager.base.ISettingDialog
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.dialog.RationaleDialogImpl
import com.proxy.service.permission.info.dialog.SettingDialogImpl
import com.proxy.service.permission.info.request.PermissionRequestImpl
import com.proxy.service.permission.info.utils.PermissionUtils


/**
 * @author: cangHX
 * @data: 2024/11/18 10:30
 * @desc:
 */
@CloudApiService(serviceTag = "service/permission")
class PermissionServiceImpl : PermissionService {

    private val tag = "${Config.LOG_TAG_START}Service"

    /**
     * 是否具有对应权限
     * */
    override fun isPermissionGranted(permission: String): Boolean {
        if (!PermissionUtils.isPermissionDeclaredInManifest(permission)) {
            CsLogger.tag(tag)
                .e("The permission is not registered in the manifest. permission: $permission")
        }
        return PermissionUtils.isPermissionGranted(permission)
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

    override fun createRationaleDialog(permissions: Array<String>): IRationaleDialog {
        permissions.forEach {
            if (!PermissionUtils.isPermissionDeclaredInManifest(it)) {
                CsLogger.tag(tag)
                    .e("The permission is not registered in the manifest. permission: $it")
            }
        }

        val dialog = RationaleDialogImpl(permissions)
            .setLeftButton(text = "取消")
            .setRightButton(text = "同意")
        return dialog
    }

    override fun createSettingDialog(permissions: Array<String>): ISettingDialog {
        permissions.forEach {
            if (!PermissionUtils.isPermissionDeclaredInManifest(it)) {
                CsLogger.tag(tag)
                    .e("The permission is not registered in the manifest. permission: $it")
            }
        }

        val dialog = SettingDialogImpl(permissions)
            .setLeftButton(text = "取消")
            .setRightButton(text = "去设置")
        return dialog
    }
}
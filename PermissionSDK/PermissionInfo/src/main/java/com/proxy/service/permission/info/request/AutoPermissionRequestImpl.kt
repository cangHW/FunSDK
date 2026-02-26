package com.proxy.service.permission.info.request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.constants.PermConstants
import com.proxy.service.permission.base.manager.IAutoPermissionRequest
import com.proxy.service.permission.info.dialog.RationaleDialogImpl
import com.proxy.service.permission.info.dialog.SettingDialogImpl
import com.proxy.service.permission.info.utils.PermissionUtils

/**
 * @author: cangHX
 * @data: 2026/2/10 17:49
 * @desc:
 */
class AutoPermissionRequestImpl : IAutoPermissionRequest {

    private val tag = "${PermConstants.LOG_TAG_START}IAutoRequest"

    private val grantedPermission = ArrayList<String>()
    private val deniedPermission = ArrayList<String>()
    private val noPromptPermission = ArrayList<String>()

    private var grantedCallback: ActionCallback? = null
    private var deniedCallback: ActionCallback? = null

    private var dialogRationaleTitle: String? = null
    private var dialogRationaleContent: String? = null
    private var dialogRationaleLeftButtonText: String? = null
    private var dialogRationaleLeftButtonClick: ButtonClick? = null
    private var dialogRationaleRightButtonText: String? = null
    private var dialogRationaleRightButtonClick: ButtonClick? = null

    private var dialogSettingTitle: String? = null
    private var dialogSettingContent: String? = null
    private var dialogSettingLeftButtonText: String? = null
    private var dialogSettingLeftButtonClick: ButtonClick? = null
    private var dialogSettingRightButtonText: String? = null
    private var dialogSettingRightButtonClick: ButtonClick? = null

    /**
     * 添加要申请的权限
     * */
    override fun addPermission(permission: String): IAutoPermissionRequest {
        if (PermissionUtils.isPermissionGranted(permission)) {
            grantedPermission.add(permission)
        } else {
            deniedPermission.add(permission)
        }
        return this
    }

    /**
     * 允许的权限回调
     * */
    override fun setGrantedCallback(callback: ActionCallback): IAutoPermissionRequest {
        this.grantedCallback = callback
        return this
    }

    /**
     * 拒绝的权限回调
     * */
    override fun setDeniedCallback(callback: ActionCallback): IAutoPermissionRequest {
        this.deniedCallback = callback
        return this
    }

    /**
     * 设置全部弹窗标题
     * */
    override fun setTitle(title: String?): IAutoPermissionRequest {
        this.dialogRationaleTitle = title
        this.dialogSettingTitle = title
        return this
    }

    /**
     * 设置申请权限理由弹窗标题
     * */
    override fun setDialogRationaleTitle(title: String?): IAutoPermissionRequest {
        this.dialogRationaleTitle = title
        return this
    }

    /**
     * 设置跳转权限设置弹窗标题
     * */
    override fun setDialogSettingTitle(title: String?): IAutoPermissionRequest {
        this.dialogSettingTitle = title
        return this
    }

    /**
     * 设置内容
     * */
    override fun setContent(content: String?): IAutoPermissionRequest {
        this.dialogRationaleContent = content
        this.dialogSettingContent = content
        return this
    }

    /**
     * 设置申请权限理由弹窗内容
     * */
    override fun setDialogRationaleContent(content: String?): IAutoPermissionRequest {
        this.dialogRationaleContent = content
        return this
    }

    /**
     * 设置跳转权限设置弹窗内容
     * */
    override fun setDialogSettingContent(content: String?): IAutoPermissionRequest {
        this.dialogSettingContent = content
        return this
    }

    /**
     * 设置左侧按钮
     * */
    override fun setLeftButton(text: String?, click: ButtonClick?): IAutoPermissionRequest {
        this.dialogRationaleLeftButtonText = text
        this.dialogRationaleLeftButtonClick = click
        this.dialogSettingLeftButtonText = text
        this.dialogSettingLeftButtonClick = click
        return this
    }

    /**
     * 设置申请权限理由弹窗左侧按钮
     * */
    override fun setDialogRationaleLeftButton(
        text: String?,
        click: ButtonClick?
    ): IAutoPermissionRequest {
        this.dialogRationaleLeftButtonText = text
        this.dialogRationaleLeftButtonClick = click
        return this
    }

    /**
     * 设置跳转权限设置弹窗左侧按钮
     * */
    override fun setDialogSettingLeftButton(
        text: String?,
        click: ButtonClick?
    ): IAutoPermissionRequest {
        this.dialogSettingLeftButtonText = text
        this.dialogSettingLeftButtonClick = click
        return this
    }

    /**
     * 设置右侧按钮
     * */
    override fun setRightButton(text: String?, click: ButtonClick?): IAutoPermissionRequest {
        this.dialogRationaleRightButtonText = text
        this.dialogRationaleRightButtonClick = click
        this.dialogSettingRightButtonText = text
        this.dialogSettingRightButtonClick = click
        return this
    }

    /**
     * 设置申请权限理由弹窗右侧按钮
     * */
    override fun setDialogRationaleRightButton(
        text: String?,
        click: ButtonClick?
    ): IAutoPermissionRequest {
        this.dialogRationaleRightButtonText = text
        this.dialogRationaleRightButtonClick = click
        return this
    }

    /**
     * 设置跳转权限设置弹窗右侧按钮
     * */
    override fun setDialogSettingRightButton(
        text: String?,
        click: ButtonClick?
    ): IAutoPermissionRequest {
        this.dialogSettingRightButtonText = text
        this.dialogSettingRightButtonClick = click
        return this
    }

    /**
     * 开始申请
     * */
    override fun start(activity: FragmentActivity) {
        showPermissionRequest(activity)
    }

    /**
     * 开始申请
     * */
    override fun start(fragment: Fragment) {
        fragment.activity?.let {
            start(it)
        } ?: let {
            CsLogger.tag(tag).i("the activity is null from fragment: $fragment")
        }
    }

    private fun somePermissionNoPrompt(list: Array<String>) {
        list.forEach {
            deniedPermission.remove(it)
            noPromptPermission.add(it)
        }
    }

    private val requestGrantedCallback = object : ActionCallback {
        override fun onAction(list: Array<String>) {
            list.forEach {
                deniedPermission.remove(it)
                noPromptPermission.remove(it)
                grantedPermission.add(it)
            }

            if (deniedPermission.isEmpty() && noPromptPermission.isEmpty()) {
                grantedCallback?.onAction(grantedPermission.toTypedArray())
            }
        }
    }

    private fun showPermissionRequest(activity: FragmentActivity) {
        val request = PermissionRequestImpl()
        deniedPermission.forEach {
            request.addPermission(it)
        }
        request.setGrantedCallback(requestGrantedCallback)
        request.setDeniedCallback(object : ActionCallback {
            override fun onAction(list: Array<String>) {
                showRationaleDialog(activity)
            }
        })
        request.setNoPromptCallback(object : ActionCallback {
            override fun onAction(list: Array<String>) {
                somePermissionNoPrompt(list)
                if (deniedPermission.isEmpty()) {
                    showSettingDialog(activity)
                }
            }
        })

        request.start(activity)
    }

    private fun showRationaleDialog(activity: FragmentActivity) {
        val dialog = RationaleDialogImpl()
            .setTitle(dialogRationaleTitle)
            .setContent(dialogRationaleContent)
            .setLeftButton(dialogRationaleLeftButtonText, dialogRationaleLeftButtonClick)
            .setRightButton(dialogRationaleRightButtonText, dialogRationaleRightButtonClick)
            .setGrantedCallback(requestGrantedCallback)
            .setDeniedCallback(object : ActionCallback {
                override fun onAction(list: Array<String>) {
                    showSettingDialog(activity)
                }
            })
            .setNoPromptCallback(object : ActionCallback {
                override fun onAction(list: Array<String>) {
                    somePermissionNoPrompt(list)
                    if (deniedPermission.isEmpty()) {
                        showSettingDialog(activity)
                    }
                }
            })

        deniedPermission.forEach {
            dialog.addPermission(it)
        }

        dialog.show(activity)
    }

    private fun showSettingDialog(activity: FragmentActivity) {
        val dialog = SettingDialogImpl()
            .setTitle(dialogSettingTitle)
            .setContent(dialogSettingContent)
            .setLeftButton(dialogSettingLeftButtonText, dialogSettingLeftButtonClick)
            .setRightButton(dialogSettingRightButtonText, dialogSettingRightButtonClick)
            .setGrantedCallback(requestGrantedCallback)
            .setDeniedCallback(object : ActionCallback {
                override fun onAction(list: Array<String>) {
                    deniedCallback?.onAction(list)
                }
            })

        deniedPermission.forEach {
            dialog.addPermission(it)
        }
        noPromptPermission.forEach {
            dialog.addPermission(it)
        }

        dialog.show(activity)
    }
}
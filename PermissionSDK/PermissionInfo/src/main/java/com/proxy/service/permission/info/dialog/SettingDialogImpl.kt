package com.proxy.service.permission.info.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.callback.ButtonClick.DialogInterface
import com.proxy.service.permission.base.callback.DialogDismissCallback
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.base.ISettingDialog
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.fragment.SettingFragment
import com.proxy.service.permission.info.utils.PermissionUtils

/**
 * @author: cangHX
 * @data: 2024/11/19 10:25
 * @desc:
 */
class SettingDialogImpl() : ISettingDialog {

    private val tag = "${Config.LOG_TAG_START}SettingDialog"

    private val permissions = ArrayList<String>()

    private var grantedCallback: ActionCallback? = null
    private var deniedCallback: ActionCallback? = null

    private var title: String? = null
    private var content: String? = null

    private var leftText: String? = null
    private var leftClick: ButtonClick? = null
    private var rightText: String? = null
    private var rightClick: ButtonClick? = null

    private var dialogInterface: DialogInterface? = null
    private var dialogDismissCallback: DialogDismissCallback? = null

    override fun addPermission(permission: String): ISettingDialog {
        if (!PermissionUtils.isPermissionDeclaredInManifest(permission)) {
            CsLogger.tag(tag)
                .e("The permission is not registered in the manifest. permission: $permission")
        }
        permissions.add(permission)
        return this
    }

    /**
     * 允许的权限回调
     * */
    override fun setGrantedCallback(callback: ActionCallback): ISettingDialog {
        this.grantedCallback = callback
        return this
    }

    /**
     * 拒绝的权限回调
     * */
    override fun setDeniedCallback(callback: ActionCallback): ISettingDialog {
        this.deniedCallback = callback
        return this
    }

    /**
     * 设置标题
     * */
    override fun setTitle(title: String?): ISettingDialog {
        this.title = title
        return this
    }

    /**
     * 设置内容
     * */
    override fun setContent(content: String?): ISettingDialog {
        this.content = content
        return this
    }

    /**
     * 设置左侧按钮
     *
     * @param text  默认值为: 取消, 可自定义
     * @param click 默认行为: 取消弹窗, 可自定义
     * */
    override fun setLeftButton(text: String?, click: ButtonClick?): ISettingDialog {
        if (text != null) {
            this.leftText = text
        }
        this.leftClick = click
        return this
    }

    /**
     * 设置右侧按钮
     *
     * @param text  默认值为: 同意 或 去设置, 可自定义
     * @param click 默认行为: 继续申请权限 或 跳转设置, 可自定义
     * */
    override fun setRightButton(text: String?, click: ButtonClick?): ISettingDialog {
        if (text != null) {
            this.rightText = text
        }
        this.rightClick = click
        return this
    }

    override fun setDismissCallback(callback: DialogDismissCallback): ISettingDialog {
        this.dialogDismissCallback = callback
        return this
    }

    /**
     * 展示需要权限的理由
     * */
    override fun show(fragment: Fragment) {
        fragment.activity?.let {
            show(it)
        } ?: let {
            CsLogger.tag(tag).i("the activity is null from fragment : $fragment")
        }
    }

    /**
     * 展示需要权限的理由
     * */
    override fun show(activity: FragmentActivity) {
        CsLogger.tag(tag).i("dialog is ready to show from activity : $activity")
        dialogInterface = Config.factory.showDialog(DialogFactory.MODE_SETTING,
            activity,
            title,
            content,
            leftText,
            object : ButtonClick {
                override fun onClick(dialog: DialogInterface): Boolean {
                    if (leftClick?.onClick(DialogInterfaceImpl(dialog)) == true) {
                        CsLogger.tag(tag).i("Left button click event has been taken over.")
                        return true
                    }
                    dialog.dismiss()
                    return true
                }
            },
            rightText,
            object : ButtonClick {
                override fun onClick(dialog: DialogInterface): Boolean {
                    if (rightClick?.onClick(DialogInterfaceImpl(dialog)) == true) {
                        CsLogger.tag(tag).i("Right button click event has been taken over.")
                        return true
                    }
                    dialog.dismiss()
                    CsLogger.tag(tag).i("Ready to launch setting page.")
                    if (activity.isFinishing) {
                        CsLogger.tag(tag)
                            .i("The activity is finishing, so can not launch setting page.")
                        return true
                    }
                    if (activity.isDestroyed) {
                        CsLogger.tag(tag)
                            .i("The activity is destroyed, so can not launch setting page.")
                        return true
                    }
                    try {
                        startSetting(activity.supportFragmentManager)
                    } catch (throwable: Throwable) {
                        CsLogger.tag(tag).e(throwable)
                    }
                    return true
                }
            },
            object : DialogDismissCallback {
                override fun onDismiss() {
                    dialogDismissCallback?.onDismiss()
                }
            })
    }

    override fun dismiss() {
        dialogInterface?.dismiss()
    }

    private fun startSetting(manager: FragmentManager) {
        val fragment = SettingFragment()
        permissions.forEach {
            fragment.addPermission(it)
        }
        grantedCallback?.let {
            fragment.setGrantedCallback(it)
        }
        deniedCallback?.let {
            fragment.setDeniedCallback(it)
        }

        val transaction = manager.beginTransaction()
        transaction.add(fragment, "${tag}_${System.currentTimeMillis()}")
        transaction.commitAllowingStateLoss()
    }

    private class DialogInterfaceImpl(val dialog: DialogInterface) : DialogInterface {
        override fun dismiss() {
            dialog.dismiss()
        }
    }
}
package com.proxy.service.permission.info.dialog

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.manager.ISettingDialog
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.fragment.ISetting
import com.proxy.service.permission.info.fragment.SettingFragment

/**
 * @author: cangHX
 * @data: 2024/11/19 10:25
 * @desc:
 */
class SettingDialogImpl(permissions: Array<String>) : ISettingDialog {

    private val tag = "${Config.LOG_TAG_START}Setting"
    private val fragment: ISetting = SettingFragment()

    private var title: String = "权限申请"
    private var content: String = "维持该应用运行所需要的必要权限"

    private var leftText: String = "取消"
    private var leftClick: ButtonClick? = null

    private var rightText: String = "去设置"
    private var rightClick: ButtonClick? = null

    init {
        fragment.setPermission(permissions)
    }

    /**
     * 允许的权限回调
     * */
    override fun setGrantedCallback(callback: ActionCallback): ISettingDialog {
        fragment.setGrantedCallback(callback)
        return this
    }

    /**
     * 拒绝的权限回调
     * */
    override fun setDeniedCallback(callback: ActionCallback): ISettingDialog {
        fragment.setDeniedCallback(callback)
        return this
    }

    /**
     * 设置标题
     * */
    override fun setTitle(title: String): ISettingDialog {
        this.title = title
        return this
    }

    /**
     * 设置内容
     * */
    override fun setContent(content: String): ISettingDialog {
        this.content = content
        return this
    }

    /**
     * 设置左侧按钮
     *
     * @param text  默认值为: 取消, 可自定义
     * @param click 默认行为: 取消弹窗, 可自定义
     * */
    override fun setLeftButton(text: String, click: ButtonClick): ISettingDialog {
        this.leftText = text
        this.leftClick = click
        return this
    }

    /**
     * 设置右侧按钮
     *
     * @param text  默认值为: 同意 或 去设置, 可自定义
     * @param click 默认行为: 继续申请权限 或 跳转设置, 可自定义
     * */
    override fun setRightButton(text: String, click: ButtonClick): ISettingDialog {
        this.rightText = text
        this.rightClick = click
        return this
    }

    /**
     * 展示需要权限的理由
     * */
    override fun show(fragment: Fragment) {
        fragment.activity?.let {
            show(it)
        }
    }

    /**
     * 展示需要权限的理由
     * */
    override fun show(activity: FragmentActivity) {
        if (activity.isFinishing) {
            return
        }
        if (activity.isDestroyed) {
            return
        }
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(content)
            .setNegativeButton(leftText) { dialog, _ ->
                if (leftClick != null) {
                    leftClick?.onClick(object : ButtonClick.DialogInterface {
                        override fun dismiss() {
                            dialog?.dismiss()
                        }
                    })
                    return@setNegativeButton
                }
                dialog.dismiss()
            }
            .setPositiveButton(rightText) { dialog, _ ->
                if (rightClick != null) {
                    rightClick?.onClick(object : ButtonClick.DialogInterface {
                        override fun dismiss() {
                            dialog?.dismiss()
                        }
                    })
                    return@setPositiveButton
                }
                dialog.dismiss()
                if (activity.isFinishing) {
                    return@setPositiveButton
                }
                if (activity.isDestroyed) {
                    return@setPositiveButton
                }
                startSetting(activity.supportFragmentManager)
            }.show()
    }

    private fun startSetting(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        transaction.add(fragment as Fragment, "${tag}_${System.currentTimeMillis()}")
        transaction.commitNowAllowingStateLoss()
        fragment.request()
    }
}
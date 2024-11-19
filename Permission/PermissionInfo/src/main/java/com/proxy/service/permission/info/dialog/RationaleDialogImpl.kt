package com.proxy.service.permission.info.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.manager.IRationaleDialog

/**
 * @author: cangHX
 * @data: 2024/11/19 10:25
 * @desc:
 */
class RationaleDialogImpl : IRationaleDialog {

    private var title: String = ""
    private var content: String = ""

    private var leftText: String = ""
    private var leftClick: ButtonClick? = null

    private var rightText: String = ""
    private var rightClick: ButtonClick? = null

    /**
     * 设置标题
     * */
    override fun setTitle(title: String): IRationaleDialog {
        this.title = title
        return this
    }

    /**
     * 设置内容
     * */
    override fun setContent(content: String): IRationaleDialog {
        this.content = content
        return this
    }

    /**
     * 设置左侧按钮
     *
     * @param text  默认值为: 取消, 可自定义
     * @param click 默认行为: 取消弹窗, 可自定义
     * */
    override fun setLeftButton(text: String, click: ButtonClick): IRationaleDialog {
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
    override fun setRightButton(text: String, click: ButtonClick): IRationaleDialog {
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
                leftClick?.onClick(object : ButtonClick.DialogInterface {
                    override fun dismiss() {
                        dialog?.dismiss()
                    }
                })
            }
            .setPositiveButton(rightText) { dialog, _ ->
                rightClick?.onClick(object : ButtonClick.DialogInterface {
                    override fun dismiss() {
                        dialog?.dismiss()
                    }
                })
            }.show()
    }
}
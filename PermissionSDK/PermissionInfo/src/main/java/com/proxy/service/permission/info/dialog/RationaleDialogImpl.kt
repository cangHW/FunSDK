package com.proxy.service.permission.info.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.base.manager.IRationaleDialog
import com.proxy.service.permission.info.config.Config

/**
 * @author: cangHX
 * @data: 2024/11/19 10:25
 * @desc:
 */
class RationaleDialogImpl : IRationaleDialog {

    private val tag = "${Config.LOG_TAG_START}RationaleDialog"

    private var title: String? = null
    private var content: String? = null

    private var leftText: String? = null
    private var leftClick: ButtonClick? = null

    private var rightText: String? = null
    private var rightClick: ButtonClick? = null

    /**
     * 设置标题
     * */
    override fun setTitle(title: String?): IRationaleDialog {
        this.title = title
        return this
    }

    /**
     * 设置内容
     * */
    override fun setContent(content: String?): IRationaleDialog {
        this.content = content
        return this
    }

    /**
     * 设置左侧按钮
     *
     * @param text  默认值为: 取消, 可自定义
     * @param click 默认行为: 取消弹窗, 可自定义
     * */
    override fun setLeftButton(text: String?, click: ButtonClick?): IRationaleDialog {
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
    override fun setRightButton(text: String?, click: ButtonClick?): IRationaleDialog {
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
        } ?: let {
            CsLogger.tag(tag).i("the activity is null from fragment : $fragment")
        }
    }

    /**
     * 展示需要权限的理由
     * */
    override fun show(activity: FragmentActivity) {
        CsLogger.tag(tag).i("dialog is ready to show from activity : $activity")
        Config.factory.showDialog(
            DialogFactory.MODE_RATIONALE,
            activity,
            title,
            content,
            leftText,
            object : ButtonClick {
                override fun onClick(dialog: ButtonClick.DialogInterface): Boolean {
                    if (leftClick?.onClick(DialogInterfaceImpl(dialog)) == true) {
                        return true
                    }
                    dialog.dismiss()
                    return true
                }
            },
            rightText,
            object : ButtonClick {
                override fun onClick(dialog: ButtonClick.DialogInterface): Boolean {
                    if (rightClick?.onClick(DialogInterfaceImpl(dialog)) == true) {
                        return true
                    }
                    dialog.dismiss()
                    return true
                }
            }
        )
    }

    private class DialogInterfaceImpl(val dialog: ButtonClick.DialogInterface) :
        ButtonClick.DialogInterface {
        override fun dismiss() {
            dialog.dismiss()
        }
    }
}
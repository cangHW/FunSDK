package com.proxy.service.permission.info.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.callback.ButtonClick.DialogInterface
import com.proxy.service.permission.base.manager.DialogFactory

/**
 * @author: cangHX
 * @data: 2024/11/20 09:53
 * @desc:
 */
class DialogFactoryImpl : DialogFactory {

    companion object {
        private const val TITLE: String = "权限申请"
        private const val CONTENT: String = "申请权限理由"
        private const val LEFT_BUTTON_TEXT: String = "拒绝"
        private const val RIGHT_BUTTON_TEXT: String = "同意"
    }

    /**
     * 展示弹窗
     * */
    override fun showDialog(
        mode: String,
        activity: Activity,
        title: String?,
        content: String?,
        leftButtonText: String?,
        leftButtonClick: ButtonClick,
        rightButtonText: String?,
        rightButtonClick: ButtonClick
    ) {
        if (activity.isFinishing) {
            return
        }
        if (activity.isDestroyed) {
            return
        }
        AlertDialog.Builder(activity)
            .setTitle(title ?: TITLE)
            .setMessage(content ?: CONTENT)
            .setCancelable(false)
            .setNegativeButton(leftButtonText ?: LEFT_BUTTON_TEXT) { dialog, _ ->
                leftButtonClick.onClick(object : DialogInterface {
                    override fun dismiss() {
                        dialog.dismiss()
                    }
                })
            }
            .setPositiveButton(rightButtonText ?: RIGHT_BUTTON_TEXT) { dialog, _ ->
                rightButtonClick.onClick(object : DialogInterface {
                    override fun dismiss() {
                        dialog.dismiss()
                    }
                })
            }.show()
    }
}
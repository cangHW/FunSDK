package com.proxy.service.permission.info.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.callback.ButtonClick.DialogInterface
import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.info.config.Config

/**
 * @author: cangHX
 * @data: 2024/11/20 09:53
 * @desc:
 */
class DialogFactoryImpl : DialogFactory {

    private val tag = "${Config.LOG_TAG_START}DialogFactory"

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
        CsLogger.tag(tag).i("Ready to show the dialog.")
        if (activity.isFinishing) {
            CsLogger.tag(tag).i("The activity is finishing, so dialog are no longer displayed.")
            return
        }
        if (activity.isDestroyed) {
            CsLogger.tag(tag).i("The activity is destroyed, so dialog are no longer displayed.")
            return
        }
        AlertDialog.Builder(activity)
            .setTitle(title ?: TITLE)
            .setMessage(content ?: CONTENT)
            .setCancelable(false)
            .setNegativeButton(leftButtonText ?: LEFT_BUTTON_TEXT) { dialog, _ ->
                CsLogger.tag(tag).i("The left button click.")
                leftButtonClick.onClick(object : DialogInterface {
                    override fun dismiss() {
                        CsLogger.tag(tag).i("Dialog is ready to dismiss.")
                        dialog.dismiss()
                    }
                })
            }
            .setPositiveButton(rightButtonText ?: RIGHT_BUTTON_TEXT) { dialog, _ ->
                CsLogger.tag(tag).i("The right button click.")
                rightButtonClick.onClick(object : DialogInterface {
                    override fun dismiss() {
                        CsLogger.tag(tag).i("Dialog is ready to dismiss.")
                        dialog.dismiss()
                    }
                })
            }.show()
    }
}
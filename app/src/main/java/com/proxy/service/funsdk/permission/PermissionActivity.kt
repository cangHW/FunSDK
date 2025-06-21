package com.proxy.service.funsdk.permission

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityPermissionBinding
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.callback.ButtonClick.DialogInterface
import com.proxy.service.permission.base.callback.DialogDismissCallback
import com.proxy.service.permission.base.manager.DialogFactory

/**
 * @author: cangHX
 * @data: 2024/11/18 16:40
 * @desc:
 */
class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {

    private val tag = "PermissionActivity"

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, PermissionActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        super.initView()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.container, PermissionFragment(), "test")
        ft.commit()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.check_permission -> {
                CsPermission.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE).let {
                    binding?.content?.addData("Permission", "READ_EXTERNAL_STORAGE = $it")
                }
            }

            R.id.set_dialog_factory -> {
                binding?.content?.addData("Permission", "设置弹窗工厂，用于自定义弹窗")
                CsPermission.setDialogFactory(object : DialogFactory {
                    override fun showDialog(
                        mode: String,
                        activity: Activity,
                        title: String?,
                        content: String?,
                        leftButtonText: String?,
                        leftButtonClick: ButtonClick,
                        rightButtonText: String?,
                        rightButtonClick: ButtonClick,
                        dialogDismissCallback: DialogDismissCallback
                    ): DialogInterface {

                        var dialog: AlertDialog? = null

                        val dialogInterface = object : DialogInterface {
                            override fun dismiss() {
                                dialog?.dismiss()
                            }
                        }

                        dialog = AlertDialog.Builder(activity)
                            .setTitle("TITLE")
                            .setMessage("CONTENT")
                            .setCancelable(false)
                            .setOnDismissListener {
                                dialogDismissCallback.onDismiss()
                            }
                            .setNegativeButton("LEFT_BUTTON_TEXT") { _, _ ->
                                leftButtonClick.onClick(dialogInterface)
                            }
                            .setPositiveButton("RIGHT_BUTTON_TEXT") { _, _ ->
                                rightButtonClick.onClick(dialogInterface)
                            }
                            .show()
                        return dialogInterface
                    }
                })
            }

            R.id.request_permission -> {
                binding?.content?.addData("Permission", "开始申请权限")
                CsPermission.createRequest()
                    ?.addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ?.addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.setGrantedCallback(grantedCallback)
                    ?.setDeniedCallback(deniedCallback)
                    ?.setNoPromptCallback(noPromptCallback)
                    ?.start()
            }

            R.id.request_permission_with_rationale_dialog -> {
                binding?.content?.addData("Permission", "开始展示申请权限弹窗")
                CsPermission.createRationaleDialog()
                    ?.addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ?.addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.setGrantedCallback(grantedCallback)
                    ?.setDeniedCallback(deniedCallback)
                    ?.setNoPromptCallback(noPromptCallback)
                    ?.setLeftButton(click = leftButtonClick)
                    ?.setRightButton(click = rightButtonClick)
                    ?.setDismissCallback(dialogDismissCallback)
                    ?.show(this)
            }

            R.id.request_permission_with_setting_dialog -> {
                binding?.content?.addData("Permission", "开始展示跳转设置申请权限弹窗")
                CsPermission.createSettingDialog()
                    ?.addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ?.addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ?.setGrantedCallback(grantedCallback)
                    ?.setDeniedCallback(deniedCallback)
                    ?.setLeftButton(click = leftButtonClick)
                    ?.setRightButton(click = rightButtonClick)
                    ?.setDismissCallback(dialogDismissCallback)
                    ?.show(this)
            }
        }
    }

    private val grantedCallback = object : ActionCallback {
        override fun onAction(list: Array<String>) {
            binding?.content?.addData("Permission", "权限同意 list=${list.joinToString(", ")}")
        }
    }

    private val deniedCallback = object : ActionCallback {
        override fun onAction(list: Array<String>) {
            binding?.content?.addData("Permission", "权限拒绝 list=${list.joinToString(", ")}")
        }
    }

    private val noPromptCallback = object : ActionCallback {
        override fun onAction(list: Array<String>) {
            binding?.content?.addData(
                "Permission",
                "权限拒绝并不再提示 list=${list.joinToString(", ")}"
            )
        }
    }

    private val leftButtonClick = object : ButtonClick {
        override fun onClick(dialog: DialogInterface): Boolean {
            binding?.content?.addData("Permission", "弹窗左侧按钮点击")
            return false
        }
    }

    private val rightButtonClick = object : ButtonClick {
        override fun onClick(dialog: DialogInterface): Boolean {
            binding?.content?.addData("Permission", "弹窗右侧按钮点击")
            return false
        }
    }

    private val dialogDismissCallback = object : DialogDismissCallback {
        override fun onDismiss() {
            binding?.content?.addData("Permission", "弹窗消失")
        }
    }
}
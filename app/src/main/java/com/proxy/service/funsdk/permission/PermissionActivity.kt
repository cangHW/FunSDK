package com.proxy.service.funsdk.permission

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R
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
class PermissionActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

//        val getResultLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                // Handle the Intent result here
//            }
//        }

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.check_permission -> {
                CsPermission.isPermissionGranted(Manifest.permission.CAMERA).let {
                    CsLogger.tag(tag).i("CAMERA = $it")
                }
            }

            R.id.set_dialog_factory -> {
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
                CsPermission.createRequest()
                    ?.addPermission(Manifest.permission.CAMERA)
                    ?.setGrantedCallback(grantedCallback)
                    ?.setDeniedCallback(deniedCallback)
                    ?.setNoPromptCallback(noPromptCallback)
                    ?.start()
            }

            R.id.request_permission_with_rationale_dialog -> {
                CsPermission.createRationaleDialog(arrayOf(Manifest.permission.CAMERA))
                    ?.setGrantedCallback(grantedCallback)
                    ?.setDeniedCallback(deniedCallback)
                    ?.setNoPromptCallback(noPromptCallback)
                    ?.setLeftButton(click = leftButtonClick)
                    ?.setRightButton(click = rightButtonClick)
                    ?.setDismissCallback(dialogDismissCallback)
                    ?.show(this)
            }

            R.id.request_permission_with_setting_dialog -> {
                CsPermission.createSettingDialog(arrayOf(Manifest.permission.CAMERA))
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
            CsLogger.tag(tag).i("onGrantedCallback = $list")
            Toast.makeText(this@PermissionActivity, "onGrantedCallback", Toast.LENGTH_SHORT).show()
        }
    }

    private val deniedCallback = object : ActionCallback {
        override fun onAction(list: Array<String>) {
            CsLogger.tag(tag).i("onDeniedCallback = $list")
            Toast.makeText(this@PermissionActivity, "onDeniedCallback", Toast.LENGTH_SHORT).show()
        }
    }

    private val noPromptCallback = object : ActionCallback {
        override fun onAction(list: Array<String>) {
            CsLogger.tag(tag).i("onNoPromptCallback = $list")
            Toast.makeText(this@PermissionActivity, "onNoPromptCallback", Toast.LENGTH_SHORT).show()
        }
    }

    private val leftButtonClick = object : ButtonClick {
        override fun onClick(dialog: DialogInterface): Boolean {
            Toast.makeText(this@PermissionActivity, "LeftButton click", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private val rightButtonClick = object : ButtonClick {
        override fun onClick(dialog: DialogInterface): Boolean {
            Toast.makeText(this@PermissionActivity, "RightButton click", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private val dialogDismissCallback = object : DialogDismissCallback {
        override fun onDismiss() {
            Toast.makeText(this@PermissionActivity, "dialog dismiss", Toast.LENGTH_SHORT).show()
        }
    }
}
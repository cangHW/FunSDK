package com.proxy.service.funsdk.permission

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R
import com.proxy.service.permission.base.callback.DeniedActionCallback
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.NoPromptActionCallback
import com.proxy.service.permission.base.manager.IRationaleDialog
import com.proxy.service.permission.base.manager.ISettingDialog

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
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.check_permission -> {
                CsPermission.isPermissionGranted(Manifest.permission.CAMERA).let {
                    CsLogger.tag(tag).i("CAMERA = $it")
                }
            }

            R.id.request_permission -> {
                CsPermission.createRequest()
                    ?.addPermission(Manifest.permission.CAMERA)
                    ?.setGrantedCallback(object : ActionCallback{
                        override fun onAction(list: Array<String>) {
                            CsLogger.tag(tag).i("onGrantedCallback = $list")
                        }
                    })
                    ?.setDeniedCallback(object : DeniedActionCallback {
                        override fun onAction(list: Array<String>, dialog: IRationaleDialog) {
                            CsLogger.tag(tag).i("onDeniedCallback = $list")
                            dialog.show(this@PermissionActivity)
                        }
                    })
                    ?.setNoPromptCallback(object : NoPromptActionCallback {
                        override fun onAction(list: Array<String>, dialog: ISettingDialog) {
                            CsLogger.tag(tag).i("onNoPromptCallback = $list")
                            dialog.setGrantedCallback(object :ActionCallback{
                                override fun onAction(list: Array<String>) {
                                    CsLogger.tag(tag).i("onGrantedCallback = $list")
                                }
                            })
                            dialog.setDeniedCallback(object :ActionCallback{
                                override fun onAction(list: Array<String>) {
                                    CsLogger.tag(tag).i("onDeniedCallback = $list")
                                }
                            })
                            dialog.show(this@PermissionActivity)
                        }
                    })
                    ?.start()
            }

            R.id.show_rationale -> {

            }
        }
    }
}
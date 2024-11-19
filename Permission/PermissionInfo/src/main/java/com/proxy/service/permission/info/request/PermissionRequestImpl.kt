package com.proxy.service.permission.info.request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.callback.DeniedActionCallback
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.callback.NoPromptActionCallback
import com.proxy.service.permission.base.manager.IPermissionRequest
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.dialog.RationaleDialogImpl
import com.proxy.service.permission.info.dialog.SettingDialogImpl
import com.proxy.service.permission.info.fragment.IRequest
import com.proxy.service.permission.info.fragment.RequestFragment

/**
 * @author: cangHX
 * @data: 2024/11/18 11:42
 * @desc:
 */
class PermissionRequestImpl : IPermissionRequest {

    private val tag = "${Config.LOG_TAG_START}Request"
    private val fragment: IRequest = RequestFragment()
    private var activity: FragmentActivity?=null

    /**
     * 添加要申请的权限
     * */
    override fun addPermission(permission: String): IPermissionRequest {
        if (permission.trim().isEmpty()) {
            CsLogger.tag(tag).i("permission can not be empty or blank.")
            return this
        }
        fragment.addPermission(permission)
        return this
    }

    /**
     * 允许的权限回调
     * */
    override fun setGrantedCallback(callback: ActionCallback): IPermissionRequest {
        fragment.setGrantedCallback(callback)
        return this
    }

    /**
     * 拒绝的权限回调
     * */
    override fun setDeniedCallback(callback: DeniedActionCallback): IPermissionRequest {
        fragment.setDeniedCallback(object :ActionCallback{
            override fun onAction(list: Array<String>) {
                val dialog = RationaleDialogImpl()
                    .setTitle("权限")
                    .setContent("维持该应用运行所需要的必要权限")
                    .setLeftButton("取消", object : ButtonClick {
                        override fun onClick(dialog: ButtonClick.DialogInterface) {
                            dialog.dismiss()
                        }
                    }).setRightButton("同意", object : ButtonClick {
                        override fun onClick(dialog: ButtonClick.DialogInterface) {
                            dialog.dismiss()
                            activity?.let {
                                start(it)
                            }
                        }
                    })
                callback.onAction(list, dialog)
            }
        })
        return this
    }

    /**
     * 拒绝并不再提示的权限回调
     * */
    override fun setNoPromptCallback(callback: NoPromptActionCallback): IPermissionRequest {
        fragment.setNoPromptCallback(object :ActionCallback{
            override fun onAction(list: Array<String>) {
                callback.onAction(list, SettingDialogImpl(list))
            }
        })
        return this
    }

    /**
     * 开始申请
     * */
    override fun start(activity: FragmentActivity) {
        this.activity = activity
        try {
            realRequestPermission(activity.supportFragmentManager)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    /**
     * 开始申请
     * */
    override fun start(fragment: Fragment) {
        try {
            realRequestPermission(fragment.childFragmentManager)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    /**
     * 开始申请
     * */
    override fun start() {
        CsContextManager.getTopActivity()?.let {
            if (it is FragmentActivity) {
                start(it)
            } else {
                CsLogger.tag(tag).i("The top activity is not a subclass of ComponentActivity.")
            }
        } ?: let {
            CsLogger.tag(tag).i("The top activity is null.")
        }
    }

    private fun realRequestPermission(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        transaction.add(fragment as Fragment, "${tag}_${System.currentTimeMillis()}")
        transaction.commitNowAllowingStateLoss()
        fragment.request()
    }
}
package com.proxy.service.permission.info.request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.manager.IPermissionRequest
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.fragment.IRequest
import com.proxy.service.permission.info.fragment.RequestFragment
import com.proxy.service.permission.info.utils.PermissionUtils

/**
 * @author: cangHX
 * @data: 2024/11/18 11:42
 * @desc:
 */
class PermissionRequestImpl : IPermissionRequest {

    private val tag = "${Config.LOG_TAG_START}IRequest"
    private val fragment: IRequest = RequestFragment()
    private var activity: FragmentActivity? = null

    /**
     * 添加要申请的权限
     * */
    override fun addPermission(permission: String): IPermissionRequest {
        if (permission.trim().isEmpty()) {
            CsLogger.tag(tag).i("permission can not be empty or blank.")
            return this
        }
        if (!PermissionUtils.isPermissionDeclaredInManifest(permission)){
            CsLogger.tag(tag).e("The permission is not registered in the manifest. permission: $permission")
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

    override fun setDeniedCallback(callback: ActionCallback): IPermissionRequest {
        fragment.setDeniedCallback(callback)
        return this
    }

    override fun setNoPromptCallback(callback: ActionCallback): IPermissionRequest {
        fragment.setNoPromptCallback(callback)
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
                CsLogger.tag(tag).i("The top activity is not a subclass of FragmentActivity.")
            }
        } ?: let {
            CsLogger.tag(tag).i("The top activity is null.")
        }
    }

    private fun realRequestPermission(manager: FragmentManager) {
        val transaction = manager.beginTransaction()
        transaction.add(fragment as Fragment, "${tag}_${System.currentTimeMillis()}")
        transaction.commitAllowingStateLoss()
    }
}
package com.proxy.service.permission.info.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.manager.base.IPermissionCallback
import com.proxy.service.permission.base.manager.base.IShouldShowRequestRationaleCallback
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.utils.PermissionUtils
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/18 18:10
 * @desc:
 */
class RequestFragment : Fragment(), IPermissionCallback<Unit>,
    IShouldShowRequestRationaleCallback<Unit> {

    private val tag = "${Config.LOG_TAG_START}Request"

    private val requestCode: Int by lazy {
        Config.REQUEST_CODE.incrementAndGet()
    }

    private val grantedPermission = ArrayList<String>()
    private var grantedAction: ActionCallback? = null

    private val deniedPermission = ArrayList<String>()
    private var deniedAction: ActionCallback? = null

    private val noPromptPermission = ArrayList<String>()
    private var noPromptAction: ActionCallback? = null

    /**
     * 添加要申请的权限
     * */
    override fun addPermission(permission: String) {
        if (PermissionUtils.isPermissionGranted(permission)) {
            grantedPermission.add(permission)
        } else {
            deniedPermission.add(permission)
        }
    }

    /**
     * 允许的权限回调
     * */
    override fun setGrantedCallback(callback: ActionCallback) {
        this.grantedAction = callback
    }

    /**
     * 拒绝的权限回调
     * */
    override fun setDeniedCallback(callback: ActionCallback) {
        this.deniedAction = callback
    }

    /**
     * 拒绝并不再提示的权限回调
     * */
    override fun setNoPromptCallback(callback: ActionCallback) {
        this.noPromptAction = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        request()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != this.requestCode) {
            return
        }
        CsLogger.tag(tag).i("result from user selection or system.")
        if (permissions.isEmpty()) {
            callback()
            return
        }

        for (i in grantResults.indices) {
            val permission = permissions[i]

            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                CsLogger.tag(tag).i("granted permission. permission: $permission")
                grantedPermission.add(permission)
                deniedPermission.remove(permission)
                continue
            }

            if (shouldShowRequestPermissionRationale(permission)) {
                continue
            }

            CsLogger.tag(tag).e("system prohibits reapplication. permission: $permission")
            noPromptPermission.add(permission)
            deniedPermission.remove(permission)
        }

        callback()
    }

    /**
     * 开始申请权限
     */
    private fun request() {
        if (deniedPermission.isEmpty()) {
            callback()
            return
        }
        CsLogger.tag(tag).i("start to request permission.")
        requestPermissions(deniedPermission.toTypedArray(), requestCode)
    }

    private fun clear() {
        parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

    private fun callback() {
        clear()
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (grantedPermission.isNotEmpty()) {
                    grantedAction?.onAction(grantedPermission.toTypedArray())
                }

                if (deniedPermission.isNotEmpty()) {
                    deniedAction?.onAction(deniedPermission.toTypedArray())
                }

                if (noPromptPermission.isNotEmpty()) {
                    noPromptAction?.onAction(noPromptPermission.toTypedArray())
                }
                return ""
            }
        })?.start()
    }

}
package com.proxy.service.permission.info.fragment

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.info.config.Config
import com.proxy.service.permission.info.utils.PermissionUtils
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/19 20:05
 * @desc:
 */
class SettingFragment : Fragment(), ISetting {

    private val tag = "${Config.LOG_TAG_START}Setting"

    private val requestCode: Int by lazy {
        Config.REQUEST_CODE.incrementAndGet()
    }

    private val grantedPermission = ArrayList<String>()
    private var grantedCallback: ActionCallback? = null

    private val deniedPermission = ArrayList<String>()
    private var deniedCallback: ActionCallback? = null

    /**
     * 添加要申请的权限
     * */
    override fun setPermission(permission: Array<String>) {
        deniedPermission.addAll(permission)
    }

    /**
     * 允许的权限回调
     * */
    override fun setGrantedCallback(callback: ActionCallback) {
        this.grantedCallback = callback
    }

    /**
     * 拒绝的权限回调
     * */
    override fun setDeniedCallback(callback: ActionCallback) {
        this.deniedCallback = callback
    }

    /**
     * 开始申请权限
     */
    override fun request() {
        CsLogger.tag(tag).i("start to launch setting page.")
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(Uri.parse("package:${CsAppUtils.getPackageName()}"))
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != this.requestCode) {
            return
        }
        CsLogger.tag(tag).i("result from setting page.")
        val iterator = deniedPermission.iterator()
        while (iterator.hasNext()) {
            val permission = iterator.next()
            if (PermissionUtils.isPermissionGranted(permission)) {
                grantedPermission.add(permission)
                CsLogger.tag(tag).i("Granted from setting. permission: $permission")
                iterator.remove()
            }
        }
        callback()
    }

    private fun clear() {
        parentFragmentManager.beginTransaction().remove(this).commitNowAllowingStateLoss()
    }

    private fun callback() {
        clear()
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (grantedPermission.isNotEmpty()) {
                    grantedCallback?.onAction(grantedPermission.toTypedArray())
                }

                if (deniedPermission.isNotEmpty()) {
                    deniedCallback?.onAction(deniedPermission.toTypedArray())
                }
                return ""
            }
        })?.start()
    }
}
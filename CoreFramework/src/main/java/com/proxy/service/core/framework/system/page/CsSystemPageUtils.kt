package com.proxy.service.core.framework.system.page

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager


/**
 * 系统页面相关工具
 *
 * @author: cangHX
 * @data: 2024/9/26 10:01
 * @desc:
 */
object CsSystemPageUtils {

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名，为空默认打开当前 app 的设置页面
     */
    fun openAppSetting(packageName: String? = null) {
        val context = CsContextManager.getApplication()
        val pkg = packageName ?: CsAppUtils.getPackageName()

        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setData(Uri.parse("package:$pkg"))
        context.startActivity(intent)
    }

    /**
     * 打开应用通知设置页面
     */
    fun openNotificationSetting() {
        val context = CsContextManager.getApplication()
        val packageName = CsAppUtils.getPackageName()
        val uid = CsAppUtils.getUid()

        val intent = Intent()
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", uid)
        context.startActivity(intent)
    }

    /**
     * 打开 WIFI 设置页面
     */
    fun openWifiSetting() {
        val context = CsContextManager.getApplication()
        val intent = Intent()
        intent.setAction(Settings.ACTION_WIRELESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}
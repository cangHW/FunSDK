package com.proxy.service.core.framework.system.page

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger


/**
 * 系统页面相关工具
 *
 * @author: cangHX
 * @data: 2024/9/26 10:01
 * @desc:
 */
object CsSystemPageUtils {

    private const val TAG = "${CoreConfig.TAG}SystemPage"

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名，为空默认打开当前 app 的设置页面
     */
    fun openAppSetting(packageName: String? = null) {
        CsLogger.tag(TAG).d("openAppSetting, packageName = $packageName")

        val pkg = packageName ?: CsAppUtils.getPackageName()

        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setData(Uri.parse("package:$pkg"))

        try {
            CsContextManager.getApplication().startActivity(intent)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    /**
     * 打开应用通知设置页面
     *
     * @param packageName : 包名，为空默认打开当前 app 的设置页面
     */
    fun openNotificationSetting(packageName: String? = null) {
        CsLogger.tag(TAG).d("openNotificationSetting")

        val pkg = packageName ?: CsAppUtils.getPackageName()
        val uid = CsAppUtils.getUid()

        val intent = Intent()
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg)
        intent.putExtra("app_package", pkg)
        intent.putExtra("app_uid", uid)

        try {
            CsContextManager.getApplication().startActivity(intent)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    /**
     * 打开 WIFI 设置页面
     */
    fun openWifiSetting() {
        CsLogger.tag(TAG).d("openWifiSetting")

        val intent = Intent()
        intent.setAction(Settings.ACTION_WIRELESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            CsContextManager.getApplication().startActivity(intent)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    /**
     * 打电话
     *
     * @param phoneNumber   准备拨打的电话号码
     */
    fun openCall(phoneNumber: String?) {
        CsLogger.tag(TAG).d("openCall, phoneNumber = $phoneNumber")

        val intent = Intent()
        if (TextUtils.isEmpty(phoneNumber)) {
            intent.setAction(Intent.ACTION_CALL_BUTTON)
        } else {
            intent.setAction(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$phoneNumber"))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            CsContextManager.getApplication().startActivity(intent)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

    }
}
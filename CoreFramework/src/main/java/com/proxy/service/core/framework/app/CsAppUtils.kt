package com.proxy.service.core.framework.app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.proxy.service.core.framework.context.CsContextManager
import com.proxy.service.core.framework.log.CsLogger


/**
 * @author: cangHX
 * @data: 2024/4/28 17:22
 * @desc:
 */
object CsAppUtils {

    /**
     * 重启应用
     * */
    fun restartApp() {
        val ctx = CsContextManager.getApplication()
        val pm = ctx.packageManager
        pm.getLaunchIntentForPackage(ctx.packageName)?.let {
            val mainIntent = Intent.makeRestartActivityTask(it.component)
            ctx.startActivity(mainIntent)
        }
        Runtime.getRuntime().exit(0)
    }

    /**
     * 获取应用包名
     * */
    fun getPackageName(): String {
        return CsContextManager.getApplication().packageName
    }

    /**
     * 获取应用名称
     * */
    fun getAppName(): String {
        val ctx = CsContextManager.getApplication()
        return ctx.applicationInfo.loadLabel(ctx.packageManager).toString()
    }

    /**
     * 获取应用版本号
     * */
    fun getVersionCode(): String {
        try {
            val context = CsContextManager.getApplication()
            val packageInfo = context.packageManager.getPackageInfo(getPackageName(), 0)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "0"
        }
    }

    /**
     * 获取应用版本名称
     * */
    fun getVersionName(): String {
        try {
            val context = CsContextManager.getApplication()
            val packageInfo = context.packageManager.getPackageInfo(getPackageName(), 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "0"
        }
    }

    /**
     * 获取应用配置信息
     * */
    fun getMetaDataInApp(key: String): String {
        val ctx = CsContextManager.getApplication()
        var value = ""
        val pm: PackageManager = ctx.packageManager
        val packageName: String = ctx.packageName
        try {
            val ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            value = ai.metaData.getString(key, "")
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return value
    }

}
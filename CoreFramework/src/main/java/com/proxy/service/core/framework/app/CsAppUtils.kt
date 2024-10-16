package com.proxy.service.core.framework.app

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger


/**
 * @author: cangHX
 * @data: 2024/4/28 17:22
 * @desc:
 */
object CsAppUtils {

    private const val TAG = "${Constants.TAG}App"

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
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return "0"
        }
    }

    /**
     * 获取当前应用的目标设备 SDK 版本
     * */
    fun getTargetSdkVersion(): Int {
        var targetSdkVersion = -1
        try {
            val context = CsContextManager.getApplication()
            val packageManager: PackageManager = context.packageManager
            val info = packageManager.getPackageInfo(getPackageName(), 0)
            targetSdkVersion = info.applicationInfo.targetSdkVersion
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return targetSdkVersion
    }

    /**
     * 获取应用版本名称
     * */
    fun getVersionName(): String {
        try {
            val context = CsContextManager.getApplication()
            val packageInfo = context.packageManager.getPackageInfo(getPackageName(), 0)
            return packageInfo.versionName
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
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
            CsLogger.tag(TAG).e(throwable)
        }
        return value
    }

    /**
     * 获取应用 icon
     * */
    fun getIcon(): Drawable {
        val context = CsContextManager.getApplication()
        return context.applicationInfo.loadIcon(context.packageManager)
    }

    /**
     * 获取当前应用的唯一标识符
     * */
    fun getUid(): Int {
        return Process.myUid()
    }

    /**
     * 获取当前进程的唯一标识符
     * */
    fun getPid(): Int {
        return Process.myPid()
    }

}
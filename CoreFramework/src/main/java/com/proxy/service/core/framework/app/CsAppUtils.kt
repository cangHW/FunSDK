package com.proxy.service.core.framework.app

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


/**
 * 应用信息与操作相关工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:22
 * @desc:
 */
object CsAppUtils {

    private const val TAG = "${CoreConfig.TAG}App"

    /**
     * 重启应用
     * */
    fun restartApp() {
        val pkg = getPackageName()
        val context = CsContextManager.getApplication()
        val pm = context.packageManager
        pm.getLaunchIntentForPackage(pkg)?.let {
            val mainIntent = Intent.makeRestartActivityTask(it.component)
            context.startActivity(mainIntent)
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
     * 获取进程名称
     * */
    fun getProcessName(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }
        try {
            BufferedReader(
                InputStreamReader(
                    FileInputStream("/proc/self/cmdline"), "UTF-8"
                )
            ).use { reader ->
                val builder = StringBuilder()
                var c: Int
                while ((reader.read().also { c = it }) > 0) {
                    builder.append(c.toChar())
                }
                return builder.toString().trim()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

    /**
     * 获取应用名称
     *
     * @param packageName : 包名，为空默认为当前应用
     * */
    fun getAppName(packageName: String? = null): String {
        val context = CsContextManager.getApplication()
        val pm = context.packageManager

        if (packageName == null) {
            return context.applicationInfo.loadLabel(pm).toString()
        }

        val packageInfo = pm.getPackageInfo(packageName, 0)
        return packageInfo.applicationInfo.loadLabel(pm).toString()
    }

    /**
     * 获取应用版本号
     *
     * @param packageName : 包名，为空默认为当前应用
     * */
    fun getVersionCode(packageName: String? = null): String {
        try {
            val pkg = packageName ?: getPackageName()
            val context = CsContextManager.getApplication()
            val packageInfo = context.packageManager.getPackageInfo(pkg, 0)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return "0"
    }

    /**
     * 获取当前应用的目标设备 SDK 版本
     *
     * @param packageName : 包名，为空默认为当前应用
     * */
    fun getTargetSdkVersion(packageName: String? = null): Int {
        try {
            val pkg = packageName ?: getPackageName()
            val context = CsContextManager.getApplication()
            val packageInfo = context.packageManager.getPackageInfo(pkg, 0)
            return packageInfo.applicationInfo.targetSdkVersion
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return -1
    }

    /**
     * 获取应用版本名称
     *
     * @param packageName : 包名，为空默认为当前应用
     * */
    fun getVersionName(packageName: String? = null): String {
        try {
            val pkg = packageName ?: getPackageName()
            val context = CsContextManager.getApplication()
            val packageInfo = context.packageManager.getPackageInfo(pkg, 0)
            return packageInfo.versionName
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return "0"
    }

    /**
     * 获取应用配置信息
     *
     * @param packageName : 包名，为空默认为当前应用
     * */
    fun getMetaDataInApp(key: String, packageName: String? = null): String {
        val context = CsContextManager.getApplication()
        val pkg = packageName ?: getPackageName()
        try {
            val applicationInfo =
                context.packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA)
            return applicationInfo.metaData.getString(key, "")
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

    /**
     * 获取应用 icon
     *
     * @param packageName : 包名，为空默认为当前应用
     * */
    fun getIcon(packageName: String? = null): Drawable {
        val context = CsContextManager.getApplication()

        if (packageName == null) {
            return context.applicationInfo.loadIcon(context.packageManager)
        }

        val packageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        return applicationInfo.loadIcon(packageManager)
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

    /**
     * 获取当前是否是暗夜模式
     * */
    fun isNightMode(): Boolean {
        val uiMode = CsContextManager.getApplication().resources.configuration.uiMode
        val currentMode = uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * 获取当前是否是主进程
     * */
    fun isMainProcess(): Boolean {
        val packageName = getPackageName()
        val processName = getProcessName()
        return packageName == processName
    }

}
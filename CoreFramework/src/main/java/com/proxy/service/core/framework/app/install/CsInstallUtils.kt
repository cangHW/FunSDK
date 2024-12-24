package com.proxy.service.core.framework.app.install

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.install.bean.InstallAppInfo
import com.proxy.service.core.framework.app.install.broadcast.BroadcastReceiverImpl
import com.proxy.service.core.framework.app.install.callback.InstallReceiverListener
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.uri.ProxyProvider
import com.proxy.service.core.service.permission.CsPermission
import java.io.File

/**
 * 应用安装相关工具
 *
 * @author: cangHX
 * @data: 2024/9/23 10:31
 * @desc:
 */
object CsInstallUtils {

    private const val TAG = "${CoreConfig.TAG}Install"

    /**
     * 通过 apk 获取包名
     * */
    fun getPackageNameByApk(apkPath: String?): String? {
        if (apkPath == null) {
            return null
        }
        val context: Context = CsContextManager.getApplication()
        val packageManager: PackageManager = context.packageManager ?: return null
        var packageInfo: PackageInfo? = null
        try {
            packageInfo =
                packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        if (packageInfo == null) {
            return null
        }
        val appInfo = packageInfo.applicationInfo
        return appInfo.packageName
    }

    /**
     * 判断目标应用是否已安装
     * 需要配置权限
     *
     * <manifest
     *     .
     *     .
     *     .
     *     <queries>
     *         <!-- 以下规则均可以添加多个 -->
     *
     *         <!-- 查询特定包名应用 -->
     *         <package android:name="com.example.someapp" />
     *
     *         <!-- 查询所有已安装的应用（仅在特殊情况下使用）-->
     *         <intent>
     *             <action android:name="android.intent.action.MAIN" />
     *         </intent>
     *
     *         <!--应用商店-->
     *         <intent>
     *             <action android:name="android.intent.action.VIEW" />
     *             <data
     *                 android:host="*"
     *                 android:scheme="market" />
     *         </intent>
     *
     *         <!--拍照应用-->
     *         <intent>
     *             <action android:name="android.media.action.IMAGE_CAPTURE" />
     *         </intent>
     *
     *         <!--略-->
     *     </queries>
     * </manifest>
     *
     * */
    fun isInstallApp(packageName: String): Boolean {
        val context: Context = CsContextManager.getApplication()
        val packageManager: PackageManager = context.packageManager ?: return false
        try {
            return packageManager.getLaunchIntentForPackage(packageName) != null
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return false
    }

    /**
     * 添加安装消息回调，弱引用
     * */
    fun addWeakReceiverListener(listener: InstallReceiverListener) {
        BroadcastReceiverImpl.addWeakReceiverListener(listener)
    }

    /**
     * 移除安装消息回调
     * */
    fun removeReceiverListener(listener: InstallReceiverListener) {
        BroadcastReceiverImpl.removeReceiverListener(listener)
    }

    /**
     * 安装目标应用
     *
     * 需要权限
     * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
     *
     * */
    fun installApp(apkPath: String) {
        val file = File(apkPath)
        if (!file.exists()) {
            CsLogger.tag(TAG).e("The apk file is empty. $apkPath")
            return
        }
        val context: Context = CsContextManager.getApplication()

        if (!CsPermission.isPermissionGranted(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
            CsLogger.tag(TAG)
                .i("Please check permissions. permission: ${Manifest.permission.REQUEST_INSTALL_PACKAGES}")
        }

        val intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val type = "application/vnd.android.package-archive"

        val uri: Uri?
        val isSdkVersionReady = Build.VERSION.SDK_INT < Build.VERSION_CODES.N
        // 部分机型系统版本为7.0，但使用provier形式会崩溃。所以判断如果targetV如果<=23并且系统版本为7.0时仍然使用file://形式
        val isTargetReady =
            Build.VERSION.SDK_INT == Build.VERSION_CODES.N && CsAppUtils.getTargetSdkVersion() <= Build.VERSION_CODES.M
        if (isSdkVersionReady || isTargetReady) {
            uri = Uri.fromFile(file)
        } else {
            uri = ProxyProvider.getUriForFile(file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        if (uri == null) {
            return
        }
        intent.setDataAndType(uri, type)
        val packageManager: PackageManager = context.packageManager
        try {
            if (packageManager.queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                ).size <= 0
            ) {
                CsLogger.tag(TAG).d("install failed")
                return
            }
            context.startActivity(intent)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
    }

    /**
     * 卸载目标应用
     *
     * 需要权限
     * <uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES"/>
     *
     * */
    fun unInstallApp(packageName: String) {
        val context: Context = CsContextManager.getApplication()

        if (!CsPermission.isPermissionGranted(Manifest.permission.REQUEST_DELETE_PACKAGES)) {
            CsLogger.tag(TAG)
                .i("Please check permissions. permission: ${Manifest.permission.REQUEST_DELETE_PACKAGES}")
        }

        val intent = Intent()
        intent.setAction(Intent.ACTION_DELETE)
        intent.setData(Uri.parse("package:$packageName"))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 打开对应包名的应用
     * 需要配置权限
     *
     * <manifest
     *     .
     *     .
     *     .
     *     <queries>
     *         <!-- 以下规则均可以添加多个 -->
     *
     *         <!-- 查询特定包名应用 -->
     *         <package android:name="com.example.someapp" />
     *
     *         <!-- 查询所有已安装的应用（仅在特殊情况下使用）-->
     *         <intent>
     *             <action android:name="android.intent.action.MAIN" />
     *         </intent>
     *
     *         <!--应用商店-->
     *         <intent>
     *             <action android:name="android.intent.action.VIEW" />
     *             <data
     *                 android:host="*"
     *                 android:scheme="market" />
     *         </intent>
     *
     *         <!--拍照应用-->
     *         <intent>
     *             <action android:name="android.media.action.IMAGE_CAPTURE" />
     *         </intent>
     *
     *         <!--略-->
     *     </queries>
     * </manifest>
     *
     * */
    fun openApp(packageName: String): Boolean {
        try {
            val context: Context = CsContextManager.getApplication()
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent == null) {
                CsLogger.tag(TAG).d("open failed")
                return false
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return false
    }

    /**
     * 获取所有已安装应用
     * 需要配置权限，否则无法获取数据或获取数据不全
     *
     * <manifest
     *     .
     *     .
     *     .
     *     <queries>
     *         <!-- 以下规则均可以添加多个 -->
     *
     *         <!-- 查询特定包名应用 -->
     *         <package android:name="com.example.someapp" />
     *
     *         <!-- 查询所有已安装的应用（仅在特殊情况下使用）-->
     *         <intent>
     *             <action android:name="android.intent.action.MAIN" />
     *         </intent>
     *
     *         <!--应用商店-->
     *         <intent>
     *             <action android:name="android.intent.action.VIEW" />
     *             <data
     *                 android:host="*"
     *                 android:scheme="market" />
     *         </intent>
     *
     *         <!--拍照应用-->
     *         <intent>
     *             <action android:name="android.media.action.IMAGE_CAPTURE" />
     *         </intent>
     *
     *         <!--略-->
     *     </queries>
     * </manifest>
     *
     * */
    fun getAllInstallAppsInfo(): List<InstallAppInfo> {
        val infoList: MutableList<InstallAppInfo> = ArrayList()
        val context: Context = CsContextManager.getApplication()
        val packageManager: PackageManager = context.packageManager
        val apps = packageManager.getInstalledPackages(0)
        for (packageInfo in apps) {
            val applicationInfo = packageInfo.applicationInfo
            val appInfo =
                InstallAppInfo()
            appInfo.icon = applicationInfo.loadIcon(packageManager)
            appInfo.name = applicationInfo.loadLabel(packageManager).toString()
            appInfo.packageName = packageInfo.packageName
            appInfo.versionCode = packageInfo.versionCode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appInfo.longVersionCode = packageInfo.longVersionCode
            }
            appInfo.versionName = packageInfo.versionName
            appInfo.isInstallSd =
                (applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0
            val isUpDatedSystem =
                (applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
            val isSystem =
                (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM
            appInfo.isSystemApp = isUpDatedSystem || isSystem

            infoList.add(appInfo)
        }
        return infoList
    }
}
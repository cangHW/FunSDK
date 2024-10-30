package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.install.InstallBroadcastReceiverImpl
import com.proxy.service.core.framework.app.install.InstallStatusEnum
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.uri.CsUriUtils
import com.proxy.service.core.framework.io.uri.ProxyProvider
import com.proxy.service.funsdk.AssetUtils
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/9/23 10:15
 * @desc:
 */
class AppInstallActivity : AppCompatActivity(), InstallBroadcastReceiverImpl.ReceiverListener {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AppInstallActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var apkDir: String =
        "/storage/emulated/0/Android/data/${CsAppUtils.getPackageName()}/files/apk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_appinstall)

        AssetUtils.copyFolderFromAssets(this, "apk", apkDir) {}
        CsUriUtils.addProviderResourcePath(apkDir)
    }

    private var pkg = ""

    fun onClick(view: View) {
        when (view.id) {
            R.id.get_pkg_from_apk -> {
                pkg = CsInstallUtils.getPackageNameByApk("$apkDir/temp.apk") ?: ""
                Toast.makeText(this, pkg, Toast.LENGTH_SHORT).show()
            }

            R.id.get_all_installed -> {
                CsInstallUtils.getAllInstallAppsInfo().forEach {
                    CsLogger.d(it.toString())
                }
            }

            R.id.check_is_installed -> {
                if (TextUtils.isEmpty(pkg)) {
                    Toast.makeText(this, "未设置包名", Toast.LENGTH_SHORT).show()
                    return
                }
                CsInstallUtils.isInstallApp(pkg).let {
                    Toast.makeText(this, "目标应用是否安装：$it", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.receiver_apk_install_status -> {
                CsInstallUtils.addWeakReceiverListener(this)
            }

            R.id.install_app -> {
                CsInstallUtils.installApp("$apkDir/temp.apk")
            }

            R.id.uninstall_app -> {
                CsInstallUtils.unInstallApp(pkg)
            }

            R.id.open_installed_app -> {
                if (CsInstallUtils.isInstallApp(pkg)) {
                    CsInstallUtils.openApp(pkg)
                }
            }
        }
    }

    override fun onReceive(
        context: Context,
        installStatusEnum: InstallStatusEnum,
        packageName: String
    ) {
        if (installStatusEnum == InstallStatusEnum.PACKAGE_ADDED) {
            Toast.makeText(context, "安装：$packageName", Toast.LENGTH_SHORT).show()
        } else if (installStatusEnum == InstallStatusEnum.PACKAGE_REMOVED) {
            Toast.makeText(context, "卸载：$packageName", Toast.LENGTH_SHORT).show()
        }
    }

}
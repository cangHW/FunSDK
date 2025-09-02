package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.install.callback.InstallReceiverListener
import com.proxy.service.core.framework.app.install.status.InstallStatusEnum
import com.proxy.service.core.framework.io.uri.CsUriManager
import com.proxy.service.funsdk.AssetUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkAppinstallBinding

/**
 * @author: cangHX
 * @data: 2024/9/23 10:15
 * @desc:
 */
class AppInstallActivity : BaseActivity<ActivityFrameworkAppinstallBinding>(), InstallReceiverListener {

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

        AssetUtils.copyFolderFromAssets(this, "apk", apkDir) {}
        CsUriManager.addProviderResourcePath(apkDir)
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkAppinstallBinding {
        return ActivityFrameworkAppinstallBinding.inflate(inflater)
    }

    private var pkg = ""

    override fun onClick(view: View) {
        when (view.id) {
            R.id.get_pkg_from_apk -> {
                pkg = CsInstallUtils.getPackageNameByApk("$apkDir/temp.apk") ?: ""
                binding?.content?.addData("应用信息", "pkg = $pkg")
            }

            R.id.get_all_installed -> {
                val builder = StringBuilder("\n")

                CsInstallUtils.getAllInstallAppsInfo().forEach {
                    builder.append(it.toString()).append("\n")
                }
                binding?.content?.addData("应用信息", "已安装 $builder")
            }

            R.id.check_is_installed -> {
                if (TextUtils.isEmpty(pkg)) {
                    binding?.content?.addData("应用信息", "先点击 “从Apk获取对应包名”")
                    return
                }
                val flag = CsInstallUtils.isInstallApp(pkg)
                binding?.content?.addData("应用信息", "目标应用是否安装：$flag")
            }

            R.id.receiver_apk_install_status -> {
                CsInstallUtils.addWeakReceiverListener(this)
            }

            R.id.install_app -> {
                val apkPath = "$apkDir/temp.apk"
                CsInstallUtils.installApp(apkPath)
                binding?.content?.addData("应用信息", "安装应用 $apkPath")
            }

            R.id.uninstall_app -> {
                if (CsInstallUtils.isInstallApp(pkg)) {
                    CsInstallUtils.unInstallApp(pkg)
                    binding?.content?.addData("应用信息", "卸载应用 $pkg")
                }else{
                    binding?.content?.addData("应用信息", "应用未安装 $pkg")
                }
            }

            R.id.open_installed_app -> {
                if (CsInstallUtils.isInstallApp(pkg)) {
                    CsInstallUtils.openApp(pkg)
                    binding?.content?.addData("应用信息", "打开应用 $pkg")
                }else{
                    binding?.content?.addData("应用信息", "应用未安装 $pkg")
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
            binding?.content?.addData("应用信息", "应用安装完成 $packageName")
        } else if (installStatusEnum == InstallStatusEnum.PACKAGE_REMOVED) {
            binding?.content?.addData("应用信息", "应用卸载完成 $packageName")
        }
    }

}
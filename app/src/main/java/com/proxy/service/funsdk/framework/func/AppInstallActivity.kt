package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.io.uri.ProxyProvider
import com.proxy.service.funsdk.AssetUtils
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/9/23 10:15
 * @desc:
 */
class AppInstallActivity : AppCompatActivity() {

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
        ProxyProvider.addSecurityPaths(apkDir)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.start_install -> {
                CsInstallUtils.installApp("$apkDir/ss.apk")
            }
        }
    }

}
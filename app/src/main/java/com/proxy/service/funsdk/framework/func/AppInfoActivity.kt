package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.databinding.ActivityFrameworkAppinfoBinding

/**
 * @author: cangHX
 * @data: 2024/12/11 10:15
 * @desc:
 */
class AppInfoActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AppInfoActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var binding: ActivityFrameworkAppinfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameworkAppinfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)

        binding?.content?.setSaveFileName("CsAppUtils")
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.restart_app -> {
                binding?.content?.addData(
                    "应用操作",
                    "重启应用"
                )
                CsAppUtils.restartApp()
            }

            R.id.get_app_pkg -> {
                binding?.content?.addData("应用信息", "应用包名: ${CsAppUtils.getPackageName()}")
            }

            R.id.get_process_name -> {
                binding?.content?.addData("应用信息", "进程名称: ${CsAppUtils.getProcessName()}")
            }

            R.id.get_app_name -> {
                binding?.content?.addData("应用信息", "应用名称: ${CsAppUtils.getAppName()}")
            }

            R.id.get_app_version_code -> {
                binding?.content?.addData("应用信息", "应用版本号: ${CsAppUtils.getVersionCode()}")
            }

            R.id.get_app_version_name -> {
                binding?.content?.addData(
                    "应用信息",
                    "应用版本名称: ${CsAppUtils.getVersionName()}"
                )
            }

            R.id.get_app_target_sdk_version -> {
                binding?.content?.addData(
                    "应用信息",
                    "应用TargetSdk: ${CsAppUtils.getTargetSdkVersion()}"
                )
            }

            R.id.app_night_mode -> {
                binding?.content?.addData(
                    "应用信息",
                    "是否处于暗夜模式: ${CsAppUtils.isNightMode()}"
                )
            }

            R.id.app_main_process -> {
                binding?.content?.addData(
                    "应用信息",
                    "是否处于主进程: ${CsAppUtils.isMainProcess()}"
                )
            }
        }
    }

}
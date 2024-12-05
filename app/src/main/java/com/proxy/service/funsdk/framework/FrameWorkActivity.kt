package com.proxy.service.funsdk.framework

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.framework.func.AppInstallActivity
import com.proxy.service.funsdk.framework.func.DeviceActivity
import com.proxy.service.funsdk.framework.func.EventActivity
import com.proxy.service.funsdk.framework.func.FileActivity
import com.proxy.service.funsdk.framework.func.MonitorActivity
import com.proxy.service.funsdk.framework.func.ScreenActivity
import com.proxy.service.funsdk.framework.func.SecurityActivity
import com.proxy.service.funsdk.framework.func.SystemPageActivity

/**
 * @author: cangHX
 * @data: 2024/9/23 10:11
 * @desc:
 */
class FrameWorkActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, FrameWorkActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.device_info -> {
                DeviceActivity.launch(this)
            }

            R.id.app_install -> {
                AppInstallActivity.launch(this)
            }

            R.id.file -> {
                FileActivity.launch(this)
            }

            R.id.system_page -> {
                SystemPageActivity.launch(this)
            }

            R.id.screen -> {
                ScreenActivity.launch(this)
            }

            R.id.event -> {
                EventActivity.launch(this)
            }

            R.id.security -> {
                SecurityActivity.launch(this)
            }

            R.id.monitor -> {
                MonitorActivity.launch(this)
            }
        }
    }

}
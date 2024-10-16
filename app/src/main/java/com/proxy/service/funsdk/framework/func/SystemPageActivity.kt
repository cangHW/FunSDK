package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.system.page.CsSystemPageUtils
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/9/26 10:18
 * @desc:
 */
class SystemPageActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SystemPageActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_systempage)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.open_notification -> {
                CsSystemPageUtils.openNotificationSetting()
            }

            R.id.open_setting -> {
                CsSystemPageUtils.openAppSetting()
            }

            R.id.open_wifi -> {
                CsSystemPageUtils.openWifiSetting()
            }
        }
    }

}
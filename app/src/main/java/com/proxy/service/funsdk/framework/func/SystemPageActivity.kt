package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import com.proxy.service.core.framework.system.page.CsSystemPageUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkSystempageBinding

/**
 * @author: cangHX
 * @data: 2024/9/26 10:18
 * @desc:
 */
class SystemPageActivity : BaseActivity<ActivityFrameworkSystempageBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SystemPageActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.open_notification -> {
                CsSystemPageUtils.openNotificationSetting()
                binding?.content?.addData("系统页面", "打开应用通知设置页面")
            }

            R.id.open_setting -> {
                CsSystemPageUtils.openAppSetting()
                binding?.content?.addData("系统页面", "打开应用设置页面")
            }

            R.id.open_wifi -> {
                CsSystemPageUtils.openWifiSetting()
                binding?.content?.addData("系统页面", "打开 Wifi 设置页面")
            }

            R.id.open_call -> {
                CsSystemPageUtils.openCall(null)
                binding?.content?.addData("系统页面", "打开通话页面")
            }
        }
    }

}
package com.proxy.service.funsdk.widget.notification

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetNotificationBinding
import com.proxy.service.widget.info.notification.CsNotificationManager
import com.proxy.service.widget.info.notification.channel.config.ChannelConfig
import com.proxy.service.widget.info.notification.notification.config.NotificationConfig

/**
 * @author: cangHX
 * @data: 2025/12/24 09:59
 * @desc:
 */
class NotificationActivity : BaseActivity<ActivityWidgetNotificationBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelConfig = ChannelConfig.builder("11", "test")
            .setBypassDnd(true)
            .build()

        CsNotificationManager.register(channelConfig)


        CsPermission.createRequest()
            ?.addPermission("android.permission.POST_NOTIFICATIONS")
            ?.start(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetNotificationBinding {
        return ActivityWidgetNotificationBinding.inflate(inflater)
    }

    var num = 10000
    override fun onClick(view: View) {
        when (view.id) {
            R.id.show_notification -> {
                num++
                val config = NotificationConfig.builder("11")
                    .setContentTitle("title")
                    .setContentText("content")
                    .setGroup("IM")
                    .build()

                CsNotificationManager.sendNotification(num, config)
//                CsNotificationManager.sendGroupedNotification(num, config)

//                num++
//                val config2 = NotificationConfig.builder("11")
//                    .setContentTitle("title")
//                    .setContentText("content")
//                    .setGroup("IM2")
//                    .build()
//
////                CsNotificationManager.sendNotification(num, config)
//                CsNotificationManager.sendGroupedNotification(num, config2)
            }

            R.id.close_one_notification -> {
                CsNotificationManager.cancel(num)
            }

            R.id.clear_notification -> {
                CsNotificationManager.cancelAll()
            }
        }
    }
}
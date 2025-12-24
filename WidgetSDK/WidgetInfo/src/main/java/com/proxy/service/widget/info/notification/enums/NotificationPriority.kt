package com.proxy.service.widget.info.notification.enums

import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

/**
 * @author: cangHX
 * @data: 2025/12/16 14:43
 * @desc:通知类别, 用于控制横幅、声音、震动等效果
 */
sealed class NotificationPriority {

    /**
     * 通知显示在通知栏，会弹出横幅，并发出声音和震动（适用于重要通知）
     * */
    object HIGH : NotificationPriority() {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun getPriority(): Int {
            return NotificationManager.IMPORTANCE_HIGH
        }

        override fun getPriorityCompat(): Int {
            return NotificationCompat.PRIORITY_HIGH
        }
    }

    /**
     * 通知显示在通知栏，可能弹出横幅，并发出声音（但不震动）
     * */
    object DEFAULT : NotificationPriority() {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun getPriority(): Int {
            return NotificationManager.IMPORTANCE_DEFAULT
        }

        override fun getPriorityCompat(): Int {
            return NotificationCompat.PRIORITY_DEFAULT
        }
    }

    /**
     * 通知显示在通知栏，不会弹出横幅，也不会发出声音或震动
     * */
    object LOW : NotificationPriority() {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun getPriority(): Int {
            return NotificationManager.IMPORTANCE_LOW
        }

        override fun getPriorityCompat(): Int {
            return NotificationCompat.PRIORITY_LOW
        }
    }

    /**
     * 自定义优先级
     * */
    abstract class Custom : NotificationPriority()

    /**
     * 获取通知
     * */
    @RequiresApi(Build.VERSION_CODES.N)
    abstract fun getPriority(): Int

    /**
     * 获取通知, 支持低版本
     * */
    abstract fun getPriorityCompat(): Int

}
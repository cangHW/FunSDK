package com.proxy.service.widget.info.notification.notification.controller

import androidx.core.app.NotificationCompat

/**
 * @author: cangHX
 * @data: 2025/12/16 16:12
 * @desc:
 */
abstract class BaseController {

    /**
     * 执行
     * */
    abstract fun doRun(builder: NotificationCompat.Builder, callback: () -> Unit)

}
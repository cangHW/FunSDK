package com.proxy.service.threadpool.info.handler.manager

import android.os.Handler

/**
 * @author: cangHX
 * @data: 2024/7/3 17:48
 * @desc:
 */
interface HandlerController {

    fun getHandler(): Handler

    fun isCanUse(): Boolean

    fun close()

    fun closeSafely()

}
package com.proxy.service.threadpool.info.handler.manager

import android.os.Handler

/**
 * @author: cangHX
 * @data: 2024/7/3 17:48
 * @desc:
 */
interface HandlerController {

    /**
     * 获取线程 ID
     * */
    fun getThreadId(): Long

    fun getHandler(): Handler

    fun isCanUse(): Boolean

    /**
     * 立刻关闭
     * */
    fun close()

    /**
     * 安全关闭
     * */
    fun closeSafely()

}
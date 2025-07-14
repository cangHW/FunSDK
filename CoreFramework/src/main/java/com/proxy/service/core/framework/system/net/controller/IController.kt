package com.proxy.service.core.framework.system.net.controller

import android.os.Looper
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/7/23 11:26
 * @desc:
 */
interface IController {

    fun start()

    fun stop()

    companion object {
        const val TAG = "${CoreConfig.TAG}Net"

        fun runUiThread(runnable: Runnable) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run()
            } else {
                CsTask.mainThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        runnable.run()
                        return ""
                    }
                })?.start()
            }
        }
    }
}
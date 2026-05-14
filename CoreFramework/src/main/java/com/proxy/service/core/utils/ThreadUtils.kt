package com.proxy.service.core.utils

import android.os.Looper
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @date: 2026/5/14 16:52
 * @desc:
 */
object ThreadUtils {

    private const val TAG = "${CoreConfig.TAG}ThreadUtils"

    fun runUiThread(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                runnable.run()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
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
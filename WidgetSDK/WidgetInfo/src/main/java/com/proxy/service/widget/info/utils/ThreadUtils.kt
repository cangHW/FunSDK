package com.proxy.service.widget.info.utils

import android.os.Looper
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/7/10 14:42
 * @desc:
 */
object ThreadUtils {

    fun runOnMainThread(runnable: Runnable) {
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
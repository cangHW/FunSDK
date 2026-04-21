package com.proxy.service.camera.info.utils

import android.os.Looper
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2026/4/21 12:53
 * @desc:
 */
object ThreadUtils {

    fun postMain(runnable: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable()
        } else {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    runnable()
                    return ""
                }
            })?.start()
        }
    }

}
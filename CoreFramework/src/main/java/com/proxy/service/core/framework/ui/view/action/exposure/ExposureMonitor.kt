package com.proxy.service.core.framework.ui.view.action.exposure

import android.view.View
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/12/4 20:33
 * @desc:
 */
class ExposureMonitor(
    private var view: View?,
    private val area: Float,
    private val delayMillis: Long,
    private var callback: ControlCallback?
) {

    interface ControlCallback {
        /**
         * view 显示
         */
        fun onShow()

        /**
         * view 隐藏
         */
        fun onGone()
    }

    init {
//        CsTask.call(object :ICallable<>)
    }

}
package com.proxy.service.webview.monitor.work.request.check.start

import com.proxy.service.core.framework.collections.CsExcellentList
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.monitor.config.controller.request.RequestConfig
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.request.bean.RequestStartData
import com.proxy.service.webview.monitor.work.request.check.start.controller.CompositeController
import com.proxy.service.webview.monitor.work.request.check.start.controller.impl.RepetitionController
import com.proxy.service.webview.monitor.work.request.check.start.controller.impl.StormController

/**
 * @author: cangHX
 * @date: 2026/6/5 17:54
 * @desc: 基于请求开始事件扩展检测。
 */
class RequestStartCheck(
    requestConfig: RequestConfig
) {

    @Volatile
    private var isChecking = false

    private val startHistoryList = CsExcellentList<RequestStartData>()
    private val controller = CompositeController(
        RepetitionController(requestConfig),
        StormController(requestConfig)
    )

    private var offset = WebMonitorConstants.REQUEST_START_STORM_WINDOW_MS

    init {
        offset = controller.getKeepTime()
    }

    fun reset() {
        isChecking = false
        startHistoryList.clear()
        controller.reset()
    }

    fun onRequestStart(url: String, data: RequestStartData) {
        val time = data.startTime ?: return
        startHistoryList.runInTransaction {
            startHistoryList.putSync(data)
            while (startHistoryList.size() > WebMonitorConstants.REQUEST_START_MAX_KEEP_NUM) {
                startHistoryList.removeAtSync(0)
            }
            if (isChecking) {
                return@runInTransaction
            }
            if (startHistoryList.size() < WebMonitorConstants.REQUEST_START_MIN_NUM_FOR_CHECK) {
                return@runInTransaction
            }
            isChecking = true

            val temp = startHistoryList.getAll()
            CsTask.computationThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    try {
                        controller.clearCache()
                        temp.forEach {
                            if ((time - (it.startTime ?: 0f)) > offset) {
                                startHistoryList.removeSync(it)
                                return@forEach
                            }
                            controller.check(time, it)
                        }
                        controller.report()
                    } finally {
                        isChecking = false
                    }
                    return ""
                }
            })?.start()
        }
    }
}

package com.proxy.service.webview.monitor.work.request.check.start.controller.impl

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.webview.monitor.CsWebMonitor
import com.proxy.service.webview.monitor.config.controller.request.RequestConfig
import com.proxy.service.webview.monitor.work.request.RequestMonitor
import com.proxy.service.webview.monitor.work.request.bean.RequestStartData
import com.proxy.service.webview.monitor.work.request.check.start.controller.AbstractController
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @date: 2026/6/7 17:02
 * @desc:
 */
class StormController(
    private val requestConfig: RequestConfig
) : AbstractController() {

    private val list = ArrayList<RequestStartData>()

    private var time:String = ""

    override fun getKeepTime(): Long {
        return requestConfig.getStormMaxTime()
    }

    override fun reset() {
        super.reset()
        time = CsTimeManager.createIntervalFactory(getKeepTime()).get("F-+SsCCCms")
    }

    override fun clearCache() {
        list.clear()
    }

    override fun check(time: Float, data: RequestStartData) {
        list.add(data)
    }

    override fun report() {
        val count = requestConfig.getStormMinCount()
        if (list.size < count) {
            return
        }
        val message = "H5请求风暴: ${time}内发起${list.size}次请求 (阈值${count})"

        if (CoreConfig.isDebug && requestConfig.getRepetitionToastEnable()) {
            CsToast.show(message)
        }
        CsLogger.tag(RequestMonitor.TAG).w(message)

        CsWebMonitor.getMonitorConfig()
            .getLogRequestCallback()
            ?.onRequestStormCall(ArrayList(list))
    }
}
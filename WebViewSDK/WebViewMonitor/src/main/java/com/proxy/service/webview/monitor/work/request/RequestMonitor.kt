package com.proxy.service.webview.monitor.work.request

import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.base.BaseMonitor
import com.proxy.service.webview.monitor.work.request.bean.RequestData
import com.proxy.service.webview.monitor.work.request.bean.RequestStartData
import com.proxy.service.webview.monitor.work.request.check.start.RequestStartCheck
import com.proxy.service.webview.monitor.work.request.creator.JsCreator
import com.proxy.service.webview.monitor.work.request.creator.LogCreator

/**
 * @author: cangHX
 * @date: 2026/1/23 14:07
 * @desc: 页面请求性能监控
 */
class RequestMonitor : BaseMonitor() {

    companion object {
        const val TAG = "${WebMonitorConstants.TAG}Req"
    }

    private val requestStartCheck = RequestStartCheck(config.getLogRequestConfig())

    fun onPageStarted() {
        requestStartCheck.reset()
    }

    override fun shouldRun(): Boolean {
        return config.getLogRequestConfig().getEnable()
    }

    override fun getJs(): String {
        return JsCreator.getJs()
    }

    fun dispatchRequestStart(url: String, log: String) {
        val data = CsJsonUtils.fromJson(log, RequestStartData::class.java) ?: return
        requestStartCheck.onRequestStart(url, data)
    }

    override fun dispatchLog(url: String, log: String) {
        val data = CsJsonUtils.fromJson(log, RequestData::class.java) ?: return

        val value = LogCreator.getLog(url, data)
        CsLogger.tag(TAG).d(value)

        config.getLogRequestCallback()?.onMonitorCall(url, log)
    }

}
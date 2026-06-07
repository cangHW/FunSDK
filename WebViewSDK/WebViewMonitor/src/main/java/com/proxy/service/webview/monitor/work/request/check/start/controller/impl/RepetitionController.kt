package com.proxy.service.webview.monitor.work.request.check.start.controller.impl

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.webview.monitor.CsWebMonitor
import com.proxy.service.webview.monitor.config.controller.request.RequestConfig
import com.proxy.service.webview.monitor.work.request.RequestMonitor
import com.proxy.service.webview.monitor.work.request.bean.RequestStartData
import com.proxy.service.webview.monitor.work.request.check.start.ApiPathNormalizer
import com.proxy.service.webview.monitor.work.request.check.start.controller.AbstractController
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @date: 2026/6/7 17:00
 * @desc:
 */
class RepetitionController(
    private val requestConfig: RequestConfig
) : AbstractController() {

    private val cacheMap = HashMap<String, String>()
    private val map = HashMap<String, ArrayList<RequestStartData>>()

    private var time:String = ""

    override fun getKeepTime(): Long {
        return requestConfig.getRepetitionMaxTime()
    }

    override fun reset() {
        time = CsTimeManager.createIntervalFactory(getKeepTime()).get("F-+SsCCCms")
        cacheMap.clear()
    }

    override fun clearCache() {
        map.clear()
    }

    override fun check(time: Float, data: RequestStartData) {
        val fingerprint = ApiPathNormalizer.fingerprint(data.method, data.requestUrl)
        if (fingerprint.isBlank()) return
        var list = map.get(fingerprint)
        if (list == null) {
            list = ArrayList()
            map.put(fingerprint, list)
        }
        list.add(data)
    }

    override fun report() {
        map.forEach { (fingerprint, items) ->
            val size = items.size
            if (size < requestConfig.getRepetitionMinCount()) {
                return@forEach
            }

            val last = items.lastOrNull() ?: return@forEach
            val oldCache = cacheMap.get(fingerprint)
            if (oldCache != null && oldCache == last.requestId) {
                return@forEach
            }

            cacheMap.put(fingerprint, last.requestId ?: "")

            val parts = fingerprint.split("|", limit = 2)
            val method = parts.getOrNull(0).orEmpty()
            val path = parts.getOrNull(1).orEmpty()
            val message = "H5重复请求: ${time}内发起${size}次 $method $path"

            if (CoreConfig.isDebug && requestConfig.getRepetitionToastEnable()) {
                CsToast.show(message)
            }
            CsLogger.tag(RequestMonitor.TAG).w(message)

            CsWebMonitor.getMonitorConfig()
                .getLogRequestCallback()
                ?.onRequestRepetitionCall(ArrayList(items))
        }
    }
}
package com.proxy.service.webview.monitor.work.request.creator

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.monitor.constant.WebMonitorConstants
import com.proxy.service.webview.monitor.work.request.RequestMonitor
import com.proxy.service.webview.monitor.work.request.bean.RequestData
import com.proxy.service.webview.monitor.work.request.bean.RequestNetworkTiming
import com.proxy.service.webview.monitor.work.request.bean.RequestSizeInfo
import java.text.DecimalFormat

/**
 * @author: cangHX
 * @date: 2026/6/5 17:44
 * @desc:
 */
object LogCreator {

    fun getLog(url: String, data: RequestData): String {
        val builder = StringBuilder()
        val hookTotal = getHookTotalDuration(data)
        val statusText = getStatusText(data)

        builder.append("网络请求性能 当前页面: ")
            .append(data.pageUrl.orEmpty().ifBlank { url })
            .append("\n")

        builder.append("  请求: [")
            .append(data.requestType)
            .append("] ")
            .append(data.method)
            .append(" ")
            .append(statusText)
            .append(" 线程=")
            .append(data.content?.threadType ?: "")
            .append(" ")
            .append(formatRequestUrlForLog(data.requestUrl))
            .append("\n")

        builder.append("  总览: ")
            .append("调用层=")
            .append(formatMs(hookTotal))
            .append(" 网络层=")
            .append(formatNetworkLayerOverview(data.networkTiming))
            .append("\n")

        appendHookTiming(builder, data)
        appendNetworkTiming(builder, data)
        appendMetaInfo(builder, data)
        appendContentInfo(builder, data)
        appendErrorInfo(builder, data)
        return builder.toString()
    }

    private fun appendHookTiming(builder: StringBuilder, data: RequestData) {
        val hookTiming = data.hookTiming ?: return
        val xhr = hookTiming.xhr
        if (xhr != null) {
            // open等待：从 xhr.open(...) 到 xhr.send(...) 的耗时，通常包含设置 header、准备 body 等同步业务准备时间。
            val openWait = duration(xhr.sendTime, xhr.openTime)
            // send到完成：从 xhr.send(...) 到 readyState=4 的耗时，包含请求发出、响应接收和 XHR 完成状态触发。
            // 注意：它不是纯网络层耗时，纯网络层耗时以 networkTiming 为准。
            val sendToDone = duration(xhr.doneTime, xhr.sendTime)
            builder.append("  调用层: open等待=")
                .append(formatMs(openWait))
                .append(", send到完成=")
                .append(formatMs(sendToDone))
                .append("\n")
            return
        }

        val fetch = hookTiming.fetch
        if (fetch != null) {
            // fetch响应：从业务调用 fetch(...) 到 fetch Promise resolve，表示拿到 Response 对象的耗时。
            // 注意：它不代表完整响应体读取完成，也不包含业务 response.json() 解析耗时。
            val fetchResolve = duration(fetch.promiseResolvedTime, fetch.fetchCallTime)
            // 响应体读取：Monitor 读取 response.clone().text() 的耗时。
            // 注意：这是监控侧读取克隆响应体，不代表业务侧解析耗时。
            val bodyRead = duration(fetch.cloneBodyReadDoneTime, fetch.cloneBodyReadStartTime)
            builder.append("  调用层: fetch响应=")
                .append(formatMs(fetchResolve))
                .append(", 响应体读取=")
                .append(formatMs(bodyRead))
                .append("\n")
        }
    }

    private fun appendNetworkTiming(builder: StringBuilder, data: RequestData) {
        val networkTiming = data.networkTiming ?: return
        when (resolveDetailLevel(networkTiming)) {
            RequestNetworkTiming.DETAIL_UNAVAILABLE -> {
                return
            }

            RequestNetworkTiming.DETAIL_RESTRICTED -> {
                builder.append("  网络层: 总=")
                    .append(formatMs(networkTiming.total))
                    .append(" (细项因跨域且未配置Timing-Allow-Origin导致无法正常获取)")
                    .append("\n")
            }

            RequestNetworkTiming.DETAIL_FULL -> {
                builder.append("  网络层: 总=")
                    .append(formatMs(networkTiming.total))
                    .append(", 排队/准备=")
                    .append(formatMs(networkTiming.queueing))
                // TTFB：responseStart - requestStart，代表从请求发送到首字节返回。
                builder.append(", TTFB=")
                    .append(formatMs(networkTiming.ttfb))
                    .append(", 下载=")
                    .append(formatMs(networkTiming.responseDownload))
                    .append(", DNS=")
                    .append(formatMs(networkTiming.dns))
                    .append(", TCP=")
                    .append(formatMs(networkTiming.tcp))
                    .append(", TLS=")
                    .append(formatMs(networkTiming.tls))
                    .append("\n")
            }
        }
    }

    private fun appendMetaInfo(builder: StringBuilder, data: RequestData) {
        val size = data.size
        val protocol = data.protocol
        if (size == null && protocol == null) {
            return
        }
        val cacheText =
            if (resolveDetailLevel(data.networkTiming) == RequestNetworkTiming.DETAIL_RESTRICTED
                && isOpaqueRestrictedSize(size)
            ) {
                "未知(跨域不可见)"
            } else {
                (size?.fromCache ?: false).toString()
            }
        builder.append("  信息: 传输=")
            .append(formatBytes(size?.transferSize))
            .append(", 缓存=")
            .append(cacheText)
            .append(", 协议=")
            .append(protocol?.nextHopProtocol.orEmpty())
            .append("\n")
    }

    private fun resolveDetailLevel(networkTiming: RequestNetworkTiming?): String {
        if (networkTiming == null) {
            return RequestNetworkTiming.DETAIL_UNAVAILABLE
        }
        when (networkTiming.detailLevel) {
            RequestNetworkTiming.DETAIL_FULL,
            RequestNetworkTiming.DETAIL_RESTRICTED,
            RequestNetworkTiming.DETAIL_UNAVAILABLE -> {
                return networkTiming.detailLevel
            }
        }
        return if (networkTiming.total > 0f) {
            RequestNetworkTiming.DETAIL_FULL
        } else {
            RequestNetworkTiming.DETAIL_UNAVAILABLE
        }
    }

    private fun formatNetworkLayerOverview(networkTiming: RequestNetworkTiming?): String {
        return when (resolveDetailLevel(networkTiming)) {
            RequestNetworkTiming.DETAIL_UNAVAILABLE -> "N/A"
            RequestNetworkTiming.DETAIL_RESTRICTED -> {
                "${formatMs(networkTiming?.total)}(跨域受限,仅总耗时可用)"
            }

            RequestNetworkTiming.DETAIL_FULL -> formatMs(networkTiming?.total)
            else -> "N/A"
        }
    }

    private fun isOpaqueRestrictedSize(size: RequestSizeInfo?): Boolean {
        if (size == null) {
            return false
        }
        return size.transferSize == 0f && size.encodedBodySize == 0f && size.decodedBodySize == 0f
    }

    private fun appendContentInfo(builder: StringBuilder, data: RequestData) {
        val content = data.content ?: return
        appendIfNotBlank(builder, "  请求Header: ", content.requestHeaders)
        appendIfNotBlank(builder, "  请求Body: ", content.requestBody)
        appendIfNotBlank(builder, "  响应Header: ", content.responseHeaders)
        appendIfNotBlank(builder, "  响应Body: ", content.responseBody)
    }

    private fun appendErrorInfo(builder: StringBuilder, data: RequestData) {
        val errorInfo = data.errorInfo ?: return
        if (errorInfo.type.isNullOrBlank() && errorInfo.message.isNullOrBlank()) {
            return
        }
        builder.append("  异常: ")
            .append(errorInfo.type)
            .append(", ")
            .append(errorInfo.message)
            .append("\n")
    }

    private fun appendIfNotBlank(builder: StringBuilder, prefix: String, value: String?) {
        if (value.isNullOrBlank()) {
            return
        }
        builder.append(prefix)
            .append(value.replace("\n", ""))
            .append("\n")
    }

    private fun getHookTotalDuration(data: RequestData): Float? {
        val hookTiming = data.hookTiming ?: return null
        hookTiming.xhr?.let {
            return duration(it.doneTime, it.sendTime)
        }
        hookTiming.fetch?.let {
            return duration(it.promiseResolvedTime ?: it.promiseRejectedTime, it.fetchCallTime)
        }
        return null
    }

    private fun getStatusText(data: RequestData): String {
        val status = data.status
        if (status != null && status > 0) {
            return status.toString()
        }
        return if (data.success) {
            "成功"
        } else {
            "失败"
        }
    }

    private fun duration(end: Float?, start: Float?): Float? {
        if (end == null || start == null) {
            return null
        }
        val value = end - start
        return if (value >= 0) {
            value
        } else {
            null
        }
    }

    private fun formatRequestUrlForLog(url: String?): String {
        val raw = url.orEmpty()
        if (raw.startsWith("data:", ignoreCase = true)) {
            val mime = raw.substringAfter("data:").substringBefore(";").ifBlank { "unknown" }
            val approxKb = (raw.length / 1024).coerceAtLeast(1)
            return "data:$mime,[约${approxKb}KB]"
        }
        if (raw.startsWith("blob:", ignoreCase = true)) {
            return "blob:[本地对象]"
        }
        if (raw.length <= WebMonitorConstants.MAX_URL_LOG_LENGTH) {
            return raw
        }
        return raw.substring(0, WebMonitorConstants.MAX_URL_LOG_LENGTH) + "...[截断]"
    }

    private fun formatMs(value: Float?): String {
        if (value == null) {
            return "N/A"
        }
        try {
            return "${DecimalFormat("#.##").format(value)}ms"
        } catch (throwable: Throwable) {
            CsLogger.tag(RequestMonitor.TAG).e(throwable)
        }
        return "error"
    }

    private fun formatBytes(value: Float?): String {
        val size = value ?: 0f
        if (size < 1024f) {
            return "${DecimalFormat("#.##").format(size)}B"
        }
        return "${DecimalFormat("#.##").format(size / 1024f)}KB"
    }

}
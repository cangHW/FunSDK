package com.proxy.service.apihttp.info.request.okhttp.interceptor

import android.os.SystemClock
import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_CONNECT
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_DNS
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_PROXY_SELECT
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_REQUEST_BODY
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_REQUEST_CALL_RESPONSE
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_REQUEST_HEADERS
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_RESPONSE_BODY
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_RESPONSE_HEADERS
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_SECURE
import com.proxy.service.apihttp.info.request.okhttp.interceptor.PerformanceInterceptor.Companion.EVENT_TOTAL
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import okhttp3.Call
import okhttp3.Connection
import okhttp3.EventListener
import okhttp3.Handshake
import okhttp3.HttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @date: 2026/5/21 10:58
 * @desc:
 *
 * [EVENT_TOTAL] start
 * Interceptor start
 *
 * [EVENT_PROXY_SELECT] start/end
 * [EVENT_DNS] start/end
 * [EVENT_CONNECT] start
 * [EVENT_SECURE] start/end
 * [EVENT_CONNECT] end
 *
 * [EVENT_REQUEST_HEADERS] start/end
 * [EVENT_REQUEST_BODY] start/end
 * [EVENT_REQUEST_CALL_RESPONSE] start/end
 * [EVENT_RESPONSE_HEADERS] start/end
 * [EVENT_RESPONSE_BODY] start/end
 *
 * Interceptor end
 * [EVENT_TOTAL] end
 *
 */
class PerformanceInterceptor(
    private val listener: EventListener?
) : EventListener() {

    companion object {
        private const val TAG_EVENT = "${ApiConstants.LOG_REQUEST_TAG_START}Event"
        private const val TAG = "${ApiConstants.LOG_REQUEST_TAG_START}Per"

        private const val STATE_SUCCESS = 0
        private const val STATE_FAILED = 1
        private const val STATE_CANCEL = 2


        /**
         * 整体请求
         * */
        private const val EVENT_TOTAL = "EVENT_TOTAL"


        /**
         * 代理选择
         * */
        private const val EVENT_PROXY_SELECT = "EVENT_PROXY_SELECT"

        /**
         * DNS解析
         * */
        private const val EVENT_DNS = "EVENT_DNS"

        /**
         * 连接建立
         * */
        private const val EVENT_CONNECT = "EVENT_CONNECT"

        /**
         * TLS/SSL握手
         * */
        private const val EVENT_SECURE = "EVENT_SECURE"


        /**
         * 请求发送Header
         * */
        private const val EVENT_REQUEST_HEADERS = "EVENT_REQUEST_HEADERS"

        /**
         * 请求发送Body
         * */
        private const val EVENT_REQUEST_BODY = "EVENT_REQUEST_BODY"

        /**
         * 响应
         * */
        private const val EVENT_REQUEST_CALL_RESPONSE = "EVENT_REQUEST_CALL_RESPONSE"

        /**
         * 响应接收Header
         * */
        private const val EVENT_RESPONSE_HEADERS = "EVENT_RESPONSE_HEADERS"

        /**
         * 响应接收Body
         * */
        private const val EVENT_RESPONSE_BODY = "EVENT_RESPONSE_BODY"
    }

    private class RequestMetrics {
        val timestamps: MutableMap<String, Long> = mutableMapOf()
        val durations: MutableMap<String, String> = mutableMapOf()
    }

    // 用于存储每个请求的生命周期数据
    private val requestMetricsMap = WeakHashMap<Call, RequestMetrics>()

    private fun getMetrics(call: Call): RequestMetrics {
        return requestMetricsMap.getOrPut(call) { RequestMetrics() }
    }

    private fun formatTime(value: String?): String {
        return value ?: "N/A"
    }

    private fun recordStart(call: Call, event: String) {
        val metrics = getMetrics(call)
        metrics.timestamps[event] = SystemClock.elapsedRealtime()
    }

    private fun recordEnd(call: Call, event: String) {
        val metrics = getMetrics(call)
        val startTime = metrics.timestamps[event]
        if (startTime == null || startTime == 0L) {
            metrics.durations[event] = "N/A"
            return
        }

        val duration = SystemClock.elapsedRealtime() - startTime
        metrics.durations[event] = "${duration}ms"
    }

    private fun recordLog(call: Call, state: Int) {
        val metrics = getMetrics(call).durations
        requestMetricsMap.remove(call)

        val request = call.request()
        val url = request.url.toString()

        val builder = StringBuilder()
        builder.append("网络请求性能")
            .append(" ${formatTime(metrics[EVENT_TOTAL])}")
        when (state) {
            STATE_SUCCESS -> {
                builder.append(" success")
            }
            STATE_FAILED -> {
                builder.append(" failed")
            }
            STATE_CANCEL -> {
                builder.append(" cancel")
            }
            else -> {
                builder.append(" unknown")
            }
        }
        builder.append(" [${request.method}]")
            .append(" url: $url")
            .append("\n")
        builder.append("  连接耗时: ")
            .append("代理选择=").append(formatTime(metrics[EVENT_PROXY_SELECT])).append(", ")
            .append("DNS解析=").append(formatTime(metrics[EVENT_DNS])).append(", ")
            .append("连接建立=").append(formatTime(metrics[EVENT_CONNECT])).append(", ")
            .append("TLS/SSL握手=").append(formatTime(metrics[EVENT_SECURE])).append(", ")
            .append("\n")
        builder.append("  IO耗时: ")
            .append("发送header=").append(formatTime(metrics[EVENT_REQUEST_HEADERS])).append(", ")
            .append("发送body=").append(formatTime(metrics[EVENT_REQUEST_BODY])).append(", ")
            .append("应答=").append(formatTime(metrics[EVENT_REQUEST_CALL_RESPONSE])).append(", ")
            .append("接收header=").append(formatTime(metrics[EVENT_RESPONSE_HEADERS])).append(", ")
            .append("接收body=").append(formatTime(metrics[EVENT_RESPONSE_BODY]))

        CsLogger.tag(TAG).d(builder.toString())
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        recordStart(call, EVENT_TOTAL)

        listener?.callStart(call)
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        recordEnd(call, EVENT_TOTAL)
        recordLog(call, STATE_SUCCESS)

        listener?.callEnd(call)
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        recordEnd(call, EVENT_TOTAL)
        recordLog(call, STATE_FAILED)

        listener?.callFailed(call, ioe)
    }

    override fun canceled(call: Call) {
        super.canceled(call)
        recordEnd(call, EVENT_TOTAL)
        recordLog(call, STATE_CANCEL)

        listener?.canceled(call)
    }


    override fun proxySelectStart(call: Call, url: HttpUrl) {
        super.proxySelectStart(call, url)
        recordStart(call, EVENT_PROXY_SELECT)

        listener?.proxySelectStart(call, url)
    }

    override fun proxySelectEnd(call: Call, url: HttpUrl, proxies: List<Proxy>) {
        super.proxySelectEnd(call, url, proxies)
        recordEnd(call, EVENT_PROXY_SELECT)

        listener?.proxySelectEnd(call, url, proxies)

        if (CoreConfig.isDebug) {
            val builder = StringBuilder()
            proxies.forEach {
                if (builder.isNotEmpty()) {
                    builder.append(", ")
                }
                builder.append("{type=${it.type()}").append(" address=${it.address()}}")
            }
            CsLogger.tag(TAG_EVENT).d("proxySelect url = $url, proxies = $builder")
        }
    }


    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        recordStart(call, EVENT_DNS)

        listener?.dnsStart(call, domainName)
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        recordEnd(call, EVENT_DNS)

        listener?.dnsEnd(call, domainName, inetAddressList)

        if (CoreConfig.isDebug) {
            val builder = StringBuilder()
            inetAddressList.forEach {
                if (builder.isNotEmpty()) {
                    builder.append(", ")
                }
                builder.append("{hostName=${it.hostName}").append(" hostAddress=${it.hostAddress}}")
            }
            CsLogger.tag(TAG_EVENT).d("dns url = ${call.request().url}, inetAddress = $builder")
        }
    }


    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        recordStart(call, EVENT_CONNECT)

        listener?.connectStart(call, inetSocketAddress, proxy)
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        recordEnd(call, EVENT_CONNECT)

        listener?.connectEnd(call, inetSocketAddress, proxy, protocol)
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        recordEnd(call, EVENT_CONNECT)

        listener?.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
    }


    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)

        listener?.connectionAcquired(call, connection)
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)

        listener?.connectionReleased(call, connection)
    }


    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        recordStart(call, EVENT_SECURE)

        listener?.secureConnectStart(call)
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        recordEnd(call, EVENT_SECURE)

        listener?.secureConnectEnd(call, handshake)
    }


    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        recordStart(call, EVENT_REQUEST_HEADERS)

        listener?.requestHeadersStart(call)
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        recordEnd(call, EVENT_REQUEST_HEADERS)
        recordStart(call, EVENT_REQUEST_CALL_RESPONSE)

        listener?.requestHeadersEnd(call, request)
    }


    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        recordStart(call, EVENT_REQUEST_BODY)

        listener?.requestBodyStart(call)
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        recordEnd(call, EVENT_REQUEST_BODY)
        recordStart(call, EVENT_REQUEST_CALL_RESPONSE)

        listener?.requestBodyEnd(call, byteCount)
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        recordEnd(call, EVENT_REQUEST_CALL_RESPONSE)

        listener?.requestFailed(call, ioe)
    }


    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        recordEnd(call, EVENT_REQUEST_CALL_RESPONSE)
        recordStart(call, EVENT_RESPONSE_HEADERS)

        listener?.responseHeadersStart(call)
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        recordEnd(call, EVENT_RESPONSE_HEADERS)

        listener?.responseHeadersEnd(call, response)
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        recordStart(call, EVENT_RESPONSE_BODY)

        listener?.responseBodyStart(call)
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        recordEnd(call, EVENT_RESPONSE_BODY)

        listener?.responseBodyEnd(call, byteCount)
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)

        listener?.responseFailed(call, ioe)
    }

}
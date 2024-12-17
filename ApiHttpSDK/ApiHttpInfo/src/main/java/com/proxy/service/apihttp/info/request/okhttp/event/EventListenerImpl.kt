package com.proxy.service.apihttp.info.request.okhttp.event

import com.proxy.service.apihttp.base.constants.Constants
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
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @author: cangHX
 * @data: 2024/6/18 16:21
 * @desc:
 */
class EventListenerImpl private constructor() : EventListener() {

    companion object {
        private const val TAG = "${Constants.LOG_REQUEST_TAG_START}HttpEvent"

        private val eventListener by lazy { EventListenerImpl() }

        fun getInstance(): EventListenerImpl {
            return eventListener
        }
    }

    private val lock = ReentrantReadWriteLock()
    private val readLock = lock.readLock()
    private val writeLock = lock.writeLock()
    private val eventListeners: MutableList<EventListener> = ArrayList()

    private var startNs: Long = 0

    fun setEventListeners(eventListeners: MutableList<EventListener>) {
        writeLock.lock()
        try {
            this.eventListeners.clear()
            this.eventListeners.addAll(eventListeners)
        } finally {
            writeLock.unlock()
        }
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        startNs = System.nanoTime()
        log("callStart url = ${call.request().url}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.callStart(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun proxySelectStart(call: Call, url: HttpUrl) {
        super.proxySelectStart(call, url)
        log("proxySelectStart url = $url")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.proxySelectStart(call, url)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun proxySelectEnd(call: Call, url: HttpUrl, proxies: List<Proxy>) {
        super.proxySelectEnd(call, url, proxies)
        val builder = StringBuilder()
        proxies.forEach {
            if (builder.isNotEmpty()) {
                builder.append(", ")
            }
            builder.append("{type=${it.type()}").append(" address=${it.address()}}")
        }
        log("proxySelectEnd url = $url, proxies = $builder")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.proxySelectEnd(call, url, proxies)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        log("dnsStart url = ${call.request().url}, domainName = $domainName")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.dnsStart(call, domainName)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        val builder = StringBuilder()
        inetAddressList.forEach {
            if (builder.isNotEmpty()) {
                builder.append(", ")
            }
            builder.append("{hostName=${it.hostName}").append(" hostAddress=${it.hostAddress}}")
        }
        log("dnsEnd url = ${call.request().url}, inetAddress = $builder")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.dnsEnd(call, domainName, inetAddressList)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        log("connectStart url = ${call.request().url}, hostName = ${inetSocketAddress.hostName}, port = ${inetSocketAddress.port}, proxyType = ${proxy.type()}, proxyAddress = ${proxy.address()}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.connectStart(call, inetSocketAddress, proxy)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        log("secureConnectStart url = ${call.request().url}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.secureConnectStart(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        log("secureConnectEnd url = ${call.request().url}, ${handshake?.toString()}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.secureConnectEnd(call, handshake)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        log("connectEnd url = ${call.request().url}, hostName = ${inetSocketAddress.hostName}, port = ${inetSocketAddress.port}, proxyType = ${proxy.type()}, proxyAddress = ${proxy.address()}, protocol = ${protocol?.toString()}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.connectEnd(call, inetSocketAddress, proxy, protocol)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        log("connectFailed url = ${call.request().url}, hostName = ${inetSocketAddress.hostName}, port = ${inetSocketAddress.port}, proxyType = ${proxy.type()}, proxyAddress = ${proxy.address()}, protocol = ${protocol?.toString()}, ioe = ${ioe.message}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
        log("connectionAcquired url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.connectionAcquired(call, connection)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
        log("connectionReleased url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.connectionReleased(call, connection)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        log("requestHeadersStart url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.requestHeadersStart(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        val builder = StringBuilder()
        request.headers.toMultimap().forEach {
            it.value.forEach { value ->
                if (builder.isNotEmpty()) {
                    builder.append(", ")
                }
                builder.append("${it.key}=$value")
            }
        }
        log("requestHeadersEnd url = ${call.request().url}}, header = $builder")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.requestHeadersEnd(call, request)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        log("requestBodyStart url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.requestBodyStart(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        log("requestBodyEnd url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.requestBodyEnd(call, byteCount)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        log("requestFailed url = ${call.request().url}}, ioe = ${ioe.message}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.requestFailed(call, ioe)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        log("responseHeadersStart url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.responseHeadersStart(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        val builder = StringBuilder()
        response.headers.toMultimap().forEach {
            it.value.forEach { value ->
                if (builder.isNotEmpty()) {
                    builder.append(", ")
                }
                builder.append("${it.key}=$value")
            }
        }
        log("responseHeadersEnd url = ${call.request().url}}, header = $builder")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.responseHeadersEnd(call, response)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        log("responseBodyStart url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.responseBodyStart(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        log("responseBodyEnd url = ${call.request().url}}, byteCount = $byteCount")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.responseBodyEnd(call, byteCount)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)
        log("responseFailed url = ${call.request().url}}, ioe = ${ioe.message}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.responseFailed(call, ioe)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        log("callEnd url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.callEnd(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        log("callFailed url = ${call.request().url}}, ioe = ${ioe.message}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.callFailed(call, ioe)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun canceled(call: Call) {
        super.canceled(call)
        log("canceled url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.canceled(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun satisfactionFailure(call: Call, response: Response) {
        super.satisfactionFailure(call, response)
        log("satisfactionFailure url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.satisfactionFailure(call, response)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun cacheHit(call: Call, response: Response) {
        super.cacheHit(call, response)
        log("cacheHit url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.cacheHit(call, response)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun cacheMiss(call: Call) {
        super.cacheMiss(call)
        log("cacheMiss url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.cacheMiss(call)
            }
        } finally {
            readLock.unlock()
        }
    }

    override fun cacheConditionalHit(call: Call, cachedResponse: Response) {
        super.cacheConditionalHit(call, cachedResponse)
        log("cacheConditionalHit url = ${call.request().url}}")

        readLock.lock()
        try {
            eventListeners.forEach {
                it.cacheConditionalHit(call, cachedResponse)
            }
        } finally {
            readLock.unlock()
        }
    }

    private fun log(msg:String){
        val timeMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        CsLogger.tag(TAG).d("[$timeMs ms] $msg")
    }
}
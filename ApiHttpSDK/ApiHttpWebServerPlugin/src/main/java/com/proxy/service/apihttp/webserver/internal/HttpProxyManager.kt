package com.proxy.service.apihttp.webserver.internal

import com.proxy.service.apihttp.webserver.config.HttpProxyMode
import com.proxy.service.apihttp.webserver.model.HttpCapture
import com.proxy.service.apihttp.webserver.model.HttpProxyDecision
import com.proxy.service.core.framework.data.log.CsLogger
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * HTTP 代理配置与会话管理
 */
object HttpProxyManager {

    private const val TAG = "CsApiHttp_Proxy"
    private const val MAX_SESSIONS = 100
    private const val INTERCEPT_TIMEOUT_MS = 60_000L

    @Volatile
    private var mode: HttpProxyMode = HttpProxyMode.PASS_THROUGH

    private val sessions = ConcurrentHashMap<String, HttpCapture>()
    private val pending = ConcurrentHashMap<String, PendingIntercept>()

    private data class PendingIntercept(
        val capture: HttpCapture,
        val latch: CountDownLatch = CountDownLatch(1),
        @Volatile var decision: HttpProxyDecision? = null,
    )

    fun getMode(): HttpProxyMode = mode

    fun resetConfig() {
        mode = HttpProxyMode.PASS_THROUGH
        CsLogger.tag(TAG).d("config reset to pass-through")
    }

    fun updateConfigFromJson(config: String) {
        if (config.isBlank()) {
            return
        }
        try {
            val json = JSONObject(config)
            mode = HttpProxyMode.fromWire(json.optString("mode", HttpProxyMode.PASS_THROUGH.wireValue))
            CsLogger.tag(TAG).d("config mode=${mode.wireValue}")
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    fun configJson(): JSONObject {
        return JSONObject().put("mode", mode.wireValue)
    }

    /**
     * 记录一次 HTTP 快照；拦截模式下阻塞等待决策
     */
    fun processCapture(capture: HttpCapture): HttpProxyDecision {
        sessions[capture.id] = capture
        trimSessions()

        if (mode != HttpProxyMode.INTERCEPT) {
            return HttpProxyDecision.PassThrough
        }

        val pendingItem = PendingIntercept(capture)
        pending[capture.id] = pendingItem
        val completed = pendingItem.latch.await(INTERCEPT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
        pending.remove(capture.id)
        if (!completed) {
            CsLogger.tag(TAG).w("intercept timeout: ${capture.id}")
            return HttpProxyDecision.TimeoutPassThrough
        }
        return pendingItem.decision ?: HttpProxyDecision.TimeoutPassThrough
    }

    fun resolveDecision(sessionId: String, decision: HttpProxyDecision): Boolean {
        val pendingItem = pending[sessionId] ?: return false
        pendingItem.decision = decision
        pendingItem.latch.countDown()
        return true
    }

    fun getCapture(sessionId: String): HttpCapture? = sessions[sessionId]

    fun listCaptures(): List<HttpCapture> {
        return sessions.values.sortedByDescending { it.createdAt }
    }

    fun captureToJson(capture: HttpCapture): JSONObject {
        return JSONObject()
            .put("id", capture.id)
            .put("tag", capture.tag)
            .put("durationMs", capture.durationMs)
            .put("createdAt", capture.createdAt)
            .put("request", capture.request)
            .put("response", capture.response)
            .put("pending", pending.containsKey(capture.id))
    }

    fun listCapturesJson(): JSONArray {
        val array = JSONArray()
        listCaptures().forEach { array.put(captureToJson(it)) }
        return array
    }

    fun overviewJson(): JSONObject {
        return JSONObject()
            .put("mode", mode.wireValue)
            .put("pendingCount", pending.size)
            .put("sessions", listCapturesJson())
    }

    private fun trimSessions() {
        if (sessions.size <= MAX_SESSIONS) {
            return
        }
        listCaptures()
            .drop(MAX_SESSIONS)
            .forEach { sessions.remove(it.id) }
    }
}

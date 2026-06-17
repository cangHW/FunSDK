package com.proxy.service.debugbridge.base.session

import com.proxy.service.debugbridge.base.constants.DebugBridgeConstants
import com.proxy.service.debugbridge.base.plugin.PluginPagePaths
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc: 会话注册与阻塞等待，NanoHTTPD / AndServer 共用
 */
object SessionManager {

    private data class PendingSession(
        val session: DebugSession,
        val latch: CountDownLatch = CountDownLatch(1),
        @Volatile var decision: Decision? = null
    )

    private val sessions = ConcurrentHashMap<String, PendingSession>()

    fun register(
        pluginId: String,
        tag: String,
        payload: String,
        metadata: Map<String, String> = emptyMap()
    ): DebugSession {
        val session = DebugSession(
            id = UUID.randomUUID().toString().replace("-", ""),
            pluginId = pluginId,
            tag = tag,
            payload = payload,
            metadata = metadata
        )
        sessions[session.id] = PendingSession(session)
        return session
    }

    fun get(sessionId: String): DebugSession? {
        return sessions[sessionId]?.session
    }

    fun list(): List<DebugSession> {
        return sessions.values
            .map { it.session }
            .sortedByDescending { it.createdAt }
    }

    fun awaitDecision(sessionId: String, timeoutMs: Long): Decision {
        val pending = sessions[sessionId] ?: return Decision.TimeoutPassThrough
        val completed = pending.latch.await(timeoutMs, TimeUnit.MILLISECONDS)
        if (!completed) {
            sessions.remove(sessionId)
            return Decision.TimeoutPassThrough
        }
        return pending.decision ?: Decision.TimeoutPassThrough
    }

    fun resolve(sessionId: String, decision: Decision): Boolean {
        val pending = sessions[sessionId] ?: return false
        pending.decision = decision
        pending.latch.countDown()
        sessions.remove(sessionId)
        return true
    }

    fun sessionPath(sessionId: String, pluginId: String): String {
        return PluginPagePaths.buildSessionPageUrl(pluginId, sessionId)
    }

    fun defaultTimeoutMs(): Long {
        return DebugBridgeConstants.DEFAULT_SESSION_TIMEOUT_MS
    }
}

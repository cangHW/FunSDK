package com.proxy.service.apihttp.webserver.model

import org.json.JSONObject

/**
 * 单次 HTTP 请求/响应快照
 */
data class HttpCapture(
    val id: String,
    val tag: String,
    val durationMs: Long,
    val request: JSONObject,
    val response: JSONObject,
    val createdAt: Long = System.currentTimeMillis(),
)

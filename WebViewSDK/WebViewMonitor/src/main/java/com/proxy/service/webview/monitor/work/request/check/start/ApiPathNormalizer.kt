package com.proxy.service.webview.monitor.work.request.check.start

import java.net.URI

/**
 * @author: cangHX
 * @date: 2026/6/7
 * @desc: API path 归一化，用于请求指纹聚合。
 */
object ApiPathNormalizer {

    private val numericPathSegment = Regex("/\\d+")

    private fun normalize(requestUrl: String?): String {
        val raw = requestUrl.orEmpty()
        if (raw.isBlank()) {
            return ""
        }
        try {
            val uri = URI(raw)
            val host = uri.host.orEmpty()
            val path = uri.path.orEmpty().ifBlank { "/" }
            val normalizedPath = path.replace(numericPathSegment, "/{id}").lowercase()
            return if (host.isNotBlank()) "$host$normalizedPath" else normalizedPath
        } catch (throwable: Throwable) {
            val fallback = raw.substringBefore("?").substringBefore("#")
            return fallback.replace(numericPathSegment, "/{id}").lowercase()
        }
    }

    fun fingerprint(method: String?, requestUrl: String?): String {
        return "${method?.uppercase() ?: "GET"}|${normalize(requestUrl)}"
    }
}

package com.proxy.service.webserver.base.http

import com.proxy.service.webserver.base.constants.ApiResponseKeys

/**
 * Web 控制台 HTTP 响应抽象（与具体 HTTP 引擎解耦）
 */
data class WebServerHttpResponse(
    val statusCode: Int,
    val mimeType: String,
    val body: String,
) {

    companion object {

        fun okJson(json: String): WebServerHttpResponse {
            return WebServerHttpResponse(200, ApiResponseKeys.MIME_JSON, json)
        }

        fun okText(body: String, mimeType: String = "text/plain; charset=utf-8"): WebServerHttpResponse {
            return WebServerHttpResponse(200, mimeType, body)
        }

        fun error(statusCode: Int, message: String): WebServerHttpResponse {
            return WebServerHttpResponse(statusCode, "text/plain; charset=utf-8", message)
        }
    }
}

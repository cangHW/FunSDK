package com.proxy.service.webserver.info.server.util

import com.proxy.service.webserver.base.constants.ApiResponseKeys
import fi.iki.elonen.NanoHTTPD.Response
import fi.iki.elonen.NanoHTTPD.newFixedLengthResponse
import org.json.JSONObject
import java.io.InputStream

/**
 * 统一 JSON HTTP 响应（供 NanoServer 等 Factory 外层使用）
 */
object HttpJsonResponse {

    fun success(data: JSONObject? = null, message: String = ""): Response {
        val status = Response.Status.OK
        val json = JSONObject()
        json.put(ApiResponseKeys.CODE, ApiResponseKeys.CODE_SUCCESS)
        json.put(ApiResponseKeys.MESSAGE, message)
        if (data != null) {
            json.put(ApiResponseKeys.DATA, data)
        }
        return newFixedLengthResponse(status, ApiResponseKeys.MIME_JSON, json.toString())
    }

    fun error(status: Response.Status, message: String): Response {
        val json = JSONObject()
        json.put(ApiResponseKeys.CODE, ApiResponseKeys.CODE_ERROR)
        json.put(ApiResponseKeys.MESSAGE, message)
        return newFixedLengthResponse(status, ApiResponseKeys.MIME_JSON, json.toString())
    }

    fun stream(
        status: Response.Status,
        mimeType: String,
        data: InputStream,
        totalBytes: Long
    ): Response {
        return newFixedLengthResponse(status, mimeType, data, totalBytes)
    }
}

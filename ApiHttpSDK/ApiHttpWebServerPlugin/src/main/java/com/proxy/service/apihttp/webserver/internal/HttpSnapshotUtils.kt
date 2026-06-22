package com.proxy.service.apihttp.webserver.internal

import com.proxy.service.apihttp.webserver.model.HttpCapture
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.util.UUID

internal object HttpSnapshotUtils {

    fun buildCapture(
        request: Request,
        response: Response,
        durationMs: Long,
    ): HttpCapture {
        val requestJson = JSONObject()
        requestJson.put("method", request.method)
        requestJson.put("url", request.url.toString())
        requestJson.put("headers", headersToJson(request.headers))
        requestJson.put("body", readRequestBody(request))

        val responseJson = JSONObject()
        responseJson.put("code", response.code)
        responseJson.put("message", response.message)
        responseJson.put("headers", headersToJson(response.headers))
        responseJson.put("body", readResponseBody(response))

        val tag = "${request.method} ${request.url.encodedPath}"
        return HttpCapture(
            id = UUID.randomUUID().toString().replace("-", ""),
            tag = tag,
            durationMs = durationMs,
            request = requestJson,
            response = responseJson,
        )
    }

    private fun headersToJson(headers: okhttp3.Headers): JSONObject {
        val json = JSONObject()
        for (index in 0 until headers.size) {
            json.put(headers.name(index), headers.value(index))
        }
        return json
    }

    private fun readRequestBody(request: Request): String {
        val body = request.body ?: return ""
        return try {
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } catch (_: Throwable) {
            ""
        }
    }

    private fun readResponseBody(response: Response): String {
        val body = response.body ?: return ""
        return try {
            val source = body.source()
            source.request(Long.MAX_VALUE)
            source.buffer.clone().readUtf8()
        } catch (_: Throwable) {
            ""
        }
    }
}

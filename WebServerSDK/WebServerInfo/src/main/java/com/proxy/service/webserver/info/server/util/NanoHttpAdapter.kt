package com.proxy.service.webserver.info.server.util

import com.proxy.service.webserver.base.http.WebServerHttpRequest
import com.proxy.service.webserver.base.http.WebServerHttpResponse
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response
import fi.iki.elonen.NanoHTTPD.newFixedLengthResponse

/**
 * NanoHTTPD 会话 → [WebServerHttpRequest]
 */
class NanoHttpRequestAdapter(
    private val session: IHTTPSession,
) : WebServerHttpRequest {

    override val method: String
        get() = session.method.name

    override val uri: String
        get() = session.uri

    override val headers: Map<String, String>
        get() = session.headers

    override val queryParams: Map<String, String>
        get() = session.parms

    override fun readFormBody(): Map<String, String> {
        val body = HashMap<String, String>()
        session.parseBody(body)
        return body
    }
}

/**
 * [WebServerHttpResponse] → NanoHTTPD Response
 */
object NanoHttpResponseAdapter {

    fun toNanoResponse(response: WebServerHttpResponse): Response {
        val status = Response.Status.lookup(response.statusCode) ?: Response.Status.INTERNAL_ERROR
        return newFixedLengthResponse(status, response.mimeType, response.body)
    }
}

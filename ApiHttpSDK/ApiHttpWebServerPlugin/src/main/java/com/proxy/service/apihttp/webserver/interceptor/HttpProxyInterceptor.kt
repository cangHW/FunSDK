package com.proxy.service.apihttp.webserver.interceptor

import com.proxy.service.apihttp.webserver.internal.HttpProxyManager
import com.proxy.service.apihttp.webserver.internal.HttpSnapshotUtils
import com.proxy.service.apihttp.webserver.model.HttpProxyDecision
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * 网络响应返回时将数据转发至 Web 控制台；拦截模式下可阻塞并替换返回值
 */
class HttpProxyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startAt = System.currentTimeMillis()
        val originalResponse = chain.proceed(request)
        val durationMs = System.currentTimeMillis() - startAt

        val response = rebuildResponseWithBufferedBody(originalResponse)
        val capture = HttpSnapshotUtils.buildCapture(request, response, durationMs)
        val decision = HttpProxyManager.processCapture(capture)

        return when (decision) {
            HttpProxyDecision.PassThrough,
            HttpProxyDecision.TimeoutPassThrough -> response

            is HttpProxyDecision.Replace -> rebuildResponse(response, decision)

            is HttpProxyDecision.Abort -> throw IOException(decision.reason)
        }
    }

    private fun rebuildResponseWithBufferedBody(response: Response): Response {
        val body = response.body ?: return response
        val content = body.string()
        return response.newBuilder()
            .body(content.toResponseBody(body.contentType()))
            .build()
    }

    private fun rebuildResponse(
        original: Response,
        decision: HttpProxyDecision.Replace,
    ): Response {
        val mediaType = original.body?.contentType()
            ?: "text/plain; charset=utf-8".toMediaTypeOrNull()
        val body = decision.body.toResponseBody(mediaType)
        return original.newBuilder()
            .code(decision.code)
            .message(decision.message)
            .body(body)
            .protocol(original.protocol)
            .build()
    }
}

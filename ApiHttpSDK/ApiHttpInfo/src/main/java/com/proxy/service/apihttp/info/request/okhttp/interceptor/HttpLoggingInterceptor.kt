package com.proxy.service.apihttp.info.request.okhttp.interceptor

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/6/18 17:28
 * @desc:
 */
class HttpLoggingInterceptor : Interceptor {
    companion object {
        private const val TAG = "${Constants.LOG_REQUEST_TAG_START}HttpLogging"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val stringBuilder = StringBuilder()

        try {
            val requestBody = request.body
            val connection = chain.connection()
            stringBuilder.append(("接口请求 --> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}"))
            stringBuilder.append("\n")

            val requestHeaders = request.headers

            if (requestBody != null) {
                requestBody.contentType()?.let {
                    if (requestHeaders["Content-Type"] == null) {
                        stringBuilder.append("Content-Type: $it").append("\n")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (requestHeaders["Content-Length"] == null) {
                        stringBuilder.append("Content-Length: ${requestBody.contentLength()}")
                            .append("\n")
                    }
                }
            }

            for (i in 0 until requestHeaders.size) {
                val name = requestHeaders.name(i)
                if (
                    !"Content-Type".equals(name, ignoreCase = true)
                    && !"Content-Length".equals(name, ignoreCase = true)
                ) {
                    stringBuilder.append("$name: ${requestHeaders.value(i)}").append("\n")
                }
            }

            if (requestBody == null) {
                stringBuilder.append("--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                stringBuilder.append("--> END ${request.method} (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                stringBuilder.append("--> END ${request.method} (duplex request body omitted)")
            } else if (requestBody.isOneShot()) {
                stringBuilder.append("--> END ${request.method} (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                if (isProbablyUtf8(buffer)) {
                    stringBuilder.append("requestBody : ${buffer.readString(charset)}").append("\n")
                    stringBuilder.append("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    stringBuilder.append("--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            throw throwable
        } finally {
            CsLogger.tag(TAG).d(stringBuilder.toString())
        }

        stringBuilder.clear()

        val response: Response
        try {
            val startNs = System.nanoTime()
            try {
                response = chain.proceed(request)
            } catch (e: Throwable) {
                stringBuilder.append("<-- HTTP FAILED: $e")
                throw e
            }

            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

            val responseBody = response.body!!
            val contentLength = responseBody.contentLength()

            stringBuilder.append("接口返回 <-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms.)")
            if (response.handshake != null) {
                stringBuilder
                    .append(" ${response.protocol}")
                    .append(" ${response.handshake?.tlsVersion ?: ""}")
            }
            stringBuilder.append("\n")

            val responseHeaders = response.headers
            for (i in 0 until responseHeaders.size) {
                stringBuilder.append("${responseHeaders.name(i)}: ${responseHeaders.value(i)}")
                    .append("\n")
            }

            if (!response.promisesBody()) {
                stringBuilder.append("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                stringBuilder.append("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE)
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(responseHeaders["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                if (!isProbablyUtf8(buffer)) {
                    stringBuilder.append("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
                    stringBuilder.append("responseBody: ${buffer.clone().readString(charset)}")
                        .append("\n")
                }

                if (gzippedLength != null) {
                    stringBuilder.append("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    stringBuilder.append("<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            throw throwable
        } finally {
            CsLogger.tag(TAG).d(stringBuilder.toString())
        }

        return response
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun isProbablyUtf8(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = buffer.size.coerceAtMost(64)
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: Throwable) {
            return false
        }
    }
}
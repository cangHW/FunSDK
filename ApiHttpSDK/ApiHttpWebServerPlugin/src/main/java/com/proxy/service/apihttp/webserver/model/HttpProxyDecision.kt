package com.proxy.service.apihttp.webserver.model

/**
 * 拦截模式下的人工决策结果
 */
sealed class HttpProxyDecision {

    /** 放行，返回原始响应 */
    object PassThrough : HttpProxyDecision()

    /** 替换响应体 */
    data class Replace(
        val body: String,
        val code: Int = 200,
        val message: String = "OK",
    ) : HttpProxyDecision()

    /** 中止请求 */
    data class Abort(
        val reason: String = "Aborted by ApiHttp proxy",
    ) : HttpProxyDecision()

    /** 等待超时，自动放行 */
    object TimeoutPassThrough : HttpProxyDecision()
}

package com.proxy.service.webview.monitor.work.request.bean

/**
 * @author: cangHX
 * @date: 2026/1/26 11:32
 * @desc:
 */
class RequestData {

    /**
     * 前端监控内部生成的单次请求标识，不等同于服务端 traceId。
     */
    var requestId: String? = ""

    /**
     * 请求来源，当前支持 xhr、fetch。
     * */
    var requestType: String? = ""

    /**
     * 请求类型 post、get
     * */
    var method: String? = ""

    /**
     * 请求状态码
     * */
    var status: Int? = null

    /**
     * 请求是否成功。HTTP 4xx/5xx 会被标记为失败。
     * */
    var success: Boolean = false

    /**
     * 当前页面地址。
     * */
    var pageUrl: String? = ""

    /**
     * 请求地址。
     * */
    var requestUrl: String? = ""

    /**
     * JS hook 调用层耗时，不代表浏览器网络栈耗时。
     * */
    var hookTiming: RequestHookTiming? = null

    /**
     * 浏览器 PerformanceResourceTiming 网络层耗时。
     * */
    var networkTiming: RequestNetworkTiming? = null

    /**
     * 传输大小与缓存信息，来自 PerformanceResourceTiming。
     * */
    var size: RequestSizeInfo? = null

    /**
     * 协议相关信息，来自 PerformanceResourceTiming。
     * */
    var protocol: RequestProtocolInfo? = null

    /**
     * 请求/响应内容，已在 JS 侧按最大长度过滤。
     * */
    var content: RequestContentInfo? = null

    /**
     * 异常信息。
     * */
    var errorInfo: RequestErrorInfo? = null

}

class RequestHookTiming {

    /**
     * XHR 调用层耗时。requestType=xhr 时有值。
     * */
    var xhr: RequestXhrHookTiming? = null

    /**
     * fetch 调用层耗时。requestType=fetch 时有值。
     * */
    var fetch: RequestFetchHookTiming? = null
}

class RequestXhrHookTiming {
    /**
     * XHR open 调用时间点，不代表网络请求已发出。
     * */
    var openTime: Float? = null

    /**
     * XHR send 调用时间点，通常可作为 XHR 请求调用层起点。
     * */
    var sendTime: Float? = null

    /**
     * readyState=2，响应头已收到。
     * */
    var headersReceivedTime: Float? = null

    /**
     * readyState=3，响应体接收中。
     * */
    var loadingTime: Float? = null

    /**
     * readyState=4，XHR 完成。
     * */
    var doneTime: Float? = null
}

class RequestFetchHookTiming {
    /**
     * fetch 被业务调用的时间点。
     * */
    var fetchCallTime: Float? = null

    /**
     * fetch Promise resolve 的时间点，代表拿到 Response，不代表业务解析完成。
     * */
    var promiseResolvedTime: Float? = null

    /**
     * fetch Promise reject 的时间点。
     * */
    var promiseRejectedTime: Float? = null

    /**
     * Monitor 开始读取 response.clone().text() 的时间点。
     * */
    var cloneBodyReadStartTime: Float? = null

    /**
     * Monitor 读取 clone body 完成的时间点，不代表业务 response.json() 解析完成。
     * */
    var cloneBodyReadDoneTime: Float? = null
}

class RequestNetworkTiming {

    companion object {
        const val DETAIL_FULL = "full"
        const val DETAIL_RESTRICTED = "restricted"
        const val DETAIL_UNAVAILABLE = "unavailable"
    }

    /**
     * 网络层细项可信度：full=细项可信，restricted=仅 total 可信，unavailable=无 ResourceTiming。
     * */
    var detailLevel: String = DETAIL_UNAVAILABLE

    /**
     * ResourceTiming startTime，相对 navigationStart。
     * */
    var startTime: Float = 0f

    /**
     * 从 ResourceTiming startTime 到 requestStart 的前置耗时。
     * 通常包含浏览器调度、排队、缓存检查、Service Worker、连接复用等待等。
     * 该字段用于解释 total 中 TTFB 与下载之外的主要差值，不是业务排队耗时。
     * */
    var queueing: Float = 0f

    /**
     * 重定向耗时。
     * */
    var redirect: Float = 0f

    /**
     * DNS 查询耗时，可能因缓存或跨域限制为 0。
     * */
    var dns: Float = 0f

    /**
     * TCP 连接耗时，连接复用时通常为 0。
     * */
    var tcp: Float = 0f

    /**
     * TLS 握手耗时，非 HTTPS 或无法获取时为 0。
     * */
    var tls: Float = 0f

    /**
     * 首字节耗时，当前等同 responseStart - requestStart。
     * */
    var ttfb: Float = 0f

    /**
     * 响应体下载耗时。
     * */
    var responseDownload: Float = 0f

    /**
     * ResourceTiming 总耗时。
     * */
    var total: Float = 0f
}

class RequestSizeInfo {
    /**
     * 网络传输大小，可能受缓存和跨域限制影响。
     * */
    var transferSize: Float = 0f

    /**
     * 压缩后的响应体大小。
     * */
    var encodedBodySize: Float = 0f

    /**
     * 解压后的响应体大小。
     * */
    var decodedBodySize: Float = 0f

    /**
     * 基于 transferSize 与 encodedBodySize 的缓存命中启发式判断。
     * */
    var fromCache: Boolean = false
}

class RequestProtocolInfo {
    /**
     * 传输协议，例如 h2、http/1.1。
     * */
    var nextHopProtocol: String? = ""

    /**
     * Performance entry 的 initiatorType，例如 xmlhttprequest、fetch。
     * */
    var initiatorType: String? = ""
}

class RequestContentInfo {
    /**
     * 线程类型，当前用于标识 JS hook 运行环境，常见值为 main / work。
     * */
    var threadType: String? = ""

    /**
     * 请求 header，已按最大长度过滤。
     * */
    var requestHeaders: String? = ""

    /**
     * 请求 body，已按最大长度过滤；二进制或超长内容不完整打印。
     * */
    var requestBody: String? = ""

    /**
     * 响应 header，已按最大长度过滤。
     * */
    var responseHeaders: String? = ""

    /**
     * 响应 body，已按最大长度过滤；二进制或超长内容不完整打印。
     * */
    var responseBody: String? = ""
}

class RequestErrorInfo {
    /**
     * 异常类型，例如 httpError、fetchReject、xhrTimeout。
     * */
    var type: String? = ""

    /**
     * 异常信息。
     * */
    var message: String? = ""
}
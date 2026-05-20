package com.proxy.service.webview.monitor.work.performance.bean

/**
 * @author: cangHX
 * @date: 2026/1/25 20:46
 * @desc: 页面导航性能摘要数据
 */
class PerformancePageData {

    /**
     * 当前页面 URL。
     * */
    var pageUrl: String? = ""

    /**
     * 导航网络阶段耗时，来源于 PerformanceNavigationTiming。
     * */
    var navigationTiming: PerformancePageNavigationTiming? = null

    /**
     * DOM 解析与加载事件耗时。
     * */
    var domAndLoadTiming: PerformancePageDomAndLoadTiming? = null
}

class PerformancePageNavigationTiming {

    /**
     * 是否匹配到可用 PerformanceNavigationTiming。
     * */
    var available: Boolean = false

    /**
     * NavigationTiming startTime，相对 timeOrigin，通常为 0。
     * */
    var startTime: Float = 0f

    /**
     * 从 startTime 到 requestStart 的前置耗时。
     * 通常包含浏览器调度、排队、缓存检查、Service Worker、连接复用等待等。
     * 该字段用于解释 total 中 TTFB 与下载之外的主要差值，不是业务排队耗时。
     * */
    var queueing: Float = 0f

    /**
     * 重定向耗时，无重定向时为 0。
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
     * 首字节耗时，等同 responseStart - requestStart。
     * */
    var ttfb: Float = 0f

    /**
     * HTML 文档响应体下载耗时，等同 responseEnd - responseStart。
     * */
    var responseDownload: Float = 0f

    /**
     * HTML 文档网络阶段总耗时，等同 responseEnd - startTime。
     * 各分段之和（queueing + redirect + dns + tcp + tls + ttfb + responseDownload）应能解释该值。
     * */
    var total: Float = 0f

    /**
     * 传输协议，例如 h2、http/1.1。
     * */
    var nextHopProtocol: String? = ""
}

class PerformancePageDomAndLoadTiming {

    /**
     * 从 responseEnd 到 domInteractive 的耗时。
     * 包含 HTML 解析及阻塞 DOM 构建的子资源加载。
     * */
    var domParse: Float = 0f

    /**
     * DOMContentLoaded 事件回调执行耗时，等同 domContentLoadedEventEnd - domContentLoadedEventStart。
     * */
    var domContentLoadedEvent: Float = 0f

    /**
     * load 事件回调执行耗时，等同 loadEventEnd - loadEventStart。
     * */
    var loadEvent: Float = 0f

    /**
     * 从导航开始到 DOM 可交互的耗时，等同 domInteractive - startTime。
     * */
    var domInteractiveTotal: Float = 0f

    /**
     * 从导航开始到页面完全加载的耗时，等同 loadEventEnd - startTime。
     * */
    var loadEventTotal: Float = 0f
}

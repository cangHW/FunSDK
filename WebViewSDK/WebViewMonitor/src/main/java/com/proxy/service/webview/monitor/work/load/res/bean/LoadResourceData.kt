package com.proxy.service.webview.monitor.work.load.res.bean

/**
 * @author: cangHX
 * @date: 2026/1/30 17:51
 * @desc:
 */
class PerformanceResourceData {

    /**
     * 页面资源加载摘要。
     * */
    var summary: PerformanceResourceSummary? = null

    /**
     * 图片资源
     * */
    var img: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 脚本文件
     * */
    var script: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 样式表文件
     * */
    var css: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 字体文件
     * */
    var font: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 视频文件
     * */
    var video: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 音频文件
     * */
    var audio: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * HTML 文件
     * */
    var iframe: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 其他类型的资源
     * */
    var other: ArrayList<PerformanceResourceDataInfo>? = ArrayList()
}

class PerformanceResourceSummary {
    /**
     * 资源总数。
     * */
    var totalCount: Int = 0

    /**
     * 缓存命中资源数量。
     * */
    var cacheCount: Int = 0

    /**
     * 全部资源传输大小总和。
     * */
    var transferSize: Long = 0L

    /**
     * 最慢资源耗时。
     * */
    var maxDuration: Float = 0f

    /**
     * 慢资源 Top5。
     * */
    var slowResources: ArrayList<PerformanceResourceDataInfo>? = ArrayList()
}

class PerformanceResourceDataInfo {

    /**
     * 资源类型。
     * */
    var type: String? = ""

    /**
     * 资源 URL。
     * */
    var name: String? = ""

    /**
     * 资源加载的起始时间，相对于页面导航开始时间。
     * */
    var startTime: Float = 0f

    /**
     * 资源总耗时，等同 responseEnd - startTime。
     * */
    var total: Float = 0f

    /**
     * 请求进入 requestStart 前的浏览器调度、排队、缓存检查、连接复用等待等前置耗时。
     * */
    var queueing: Float = 0f

    /**
     * DNS 查询耗时，计算公式为 domainLookupEnd - domainLookupStart。
     * */
    var dns: Float = 0f

    /**
     * TCP 连接耗时，计算公式为 connectEnd - connectStart。
     * */
    var tcp: Float = 0f

    /**
     * TLS 握手耗时。
     * */
    var tls: Float = 0f

    /**
     * 首字节耗时，等同 responseStart - requestStart。
     * */
    var ttfb: Float = 0f

    /**
     * 响应体下载耗时，等同 responseEnd - responseStart。
     * */
    var download: Float = 0f

    /**
     * 资源的传输大小，包括 HTTP 响应头和响应体的大小。
     * */
    var transferSize: Long = 0L

    /**
     * 压缩后的响应体大小。
     * */
    var encodedBodySize: Long = 0L

    /**
     * 解压后的响应体大小。
     * */
    var decodedBodySize: Long = 0L

    /**
     * 资源是否从缓存中加载。
     * */
    var fromCache: Boolean = false

    /**
     * 资源加载时使用的网络协议（如 http/1.1、h2、h3）。
     * */
    var nextHopProtocol: String? = ""

}
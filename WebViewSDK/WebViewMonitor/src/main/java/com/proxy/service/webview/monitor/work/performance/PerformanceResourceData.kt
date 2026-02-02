package com.proxy.service.webview.monitor.work.performance

import java.text.DecimalFormat

/**
 * @author: cangHX
 * @data: 2026/1/30 17:51
 * @desc:
 */
class PerformanceResourceData {

    /**
     * 图片资源，通过 <img> 标签加载
     * */
    var img: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * JavaScript 文件，通过 <script> 标签加载
     * */
    var script: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 样式表文件，通过 <link> 标签加载
     * */
    var css: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * XHR 请求，通过 XMLHttpRequest 发起的网络请求
     * */
    var xmlhttprequest: ArrayList<PerformanceResourceDataInfo>? = ArrayList()

    /**
     * 其他类型的资源
     * */
    var other: ArrayList<PerformanceResourceDataInfo>? = ArrayList()
}

class PerformanceResourceDataInfo {

    /**
     * 资源加载的起始时间，相对于页面导航开始时间。
     * */
    var startTime: Float = 0f

    /**
     * 资源加载的持续时间，从开始加载到加载完成的时间。
     * */
    var duration: Float = 0f

    /**
     * 资源的传输大小，包括 HTTP 响应头和响应体的大小。
     * */
    var transferSize: Long = 0L

    /**
     * DNS 查询耗时，计算公式为 domainLookupEnd - domainLookupStart。
     * */
    var domainLookupStart: Float = 0f

    /**
     * DNS 查询耗时，计算公式为 domainLookupEnd - domainLookupStart。
     * */
    var domainLookupEnd: Float = 0f

    /**
     * TCP 连接耗时，计算公式为 connectEnd - connectStart。
     * */
    var connectStart: Float = 0f

    /**
     * TCP 连接耗时，计算公式为 connectEnd - connectStart。
     * */
    var connectEnd: Float = 0f

    /**
     * 浏览器开始发送请求的时间点。
     * */
    var requestStart: Float = 0f

    /**
     * 服务器响应耗时，计算公式为 responseEnd - responseStart。
     * */
    var responseStart: Float = 0f

    /**
     * 服务器响应耗时，计算公式为 responseEnd - responseStart。
     * */
    var responseEnd: Float = 0f

    /**
     * 资源是否从缓存中加载。
     * */
    var fromCache: Boolean = false

    /**
     * 资源加载时使用的网络协议（如 http/1.1、h2、h3）。
     * */
    var nextHopProtocol: String? = ""

    /**
     * 资源的 URL，表示资源的唯一标识符。
     * */
    var name: String? = ""

    override fun toString(): String {
        val format = DecimalFormat("#.##")
        val builder = StringBuilder()

        builder.append("开始加载延迟: ")
            .append(format.format(startTime))
            .append("ms")
            .append(", ")
        builder.append("加载耗时: ")
            .append(format.format(duration))
            .append("ms")
            .append(", ")
        builder.append("网络传输大小: ").append(transferSize).append("字节").append(", ")
        builder.append("是否从缓存加载: ").append(fromCache).append(", ")
        builder.append("加载协议: ").append(nextHopProtocol).append(", ")
        builder.append("DNS 解析耗时: ")
            .append(format.format(domainLookupEnd - domainLookupStart))
            .append("ms")
            .append(", ")
        builder.append("TCP 链接耗时: ")
            .append(format.format(connectEnd - connectStart))
            .append("ms")
            .append(", ")
        builder.append("服务器响应耗时: ")
            .append(format.format(responseStart - requestStart))
            .append("ms")
            .append(", ")
        builder.append("数据传输耗时: ")
            .append(format.format(responseEnd - responseStart))
            .append("ms")
            .append(", ")
        builder.append("资源: ").append(name)

        return builder.toString()
    }

}
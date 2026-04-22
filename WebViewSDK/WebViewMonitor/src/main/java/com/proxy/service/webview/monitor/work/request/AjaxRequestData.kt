package com.proxy.service.webview.monitor.work.request

/**
 * @author: cangHX
 * @data: 2026/1/26 11:32
 * @desc:
 */
class AjaxRequestData {

    /**
     * 请求类型 post、get
     * */
    var method: String? = ""

    /**
     * 线程类型
     * */
    var threadType: String? = ""

    /**
     * 整体耗时
     * */
    var tDuration: AjaxRequestH5Data? = null

    /**
     * 网络耗时
     * */
    var pDuration: AjaxRequestPerfData? = null

    /**
     * 请求链接
     * */
    var url: String? = ""

    /**
     * 请求 header
     * */
    var requestHeaders: String? = ""

    /**
     * 请求 body
     * */
    var requestBody: String? = ""

    /**
     * 应答 header
     * */
    var responseHeaders: String? = ""

    /**
     * 应答 body
     * */
    var responseBody: String? = ""

}

class AjaxRequestH5Data {
    /**
     * 请求 开始时间
     * */
    var startTime: Float = 0f

    /**
     * dns 开始时间
     * */
    var dnsStartTime: Float = 0f

    /**
     * tcp 开始时间
     * */
    var tcpStartTime: Float = 0f

    /**
     * 网络请求 开始时间
     * */
    var requestStartTime: Float = 0f

    /**
     * 应答 开始时间
     * */
    var responseStartTime: Float = 0f
}

class AjaxRequestPerfData {
    /**
     * 资源加载的起始时间，相对于页面导航开始时间。
     * */
    var startTime: Float = 0f

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
}
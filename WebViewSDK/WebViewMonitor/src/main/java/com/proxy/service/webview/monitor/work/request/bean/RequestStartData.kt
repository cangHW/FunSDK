package com.proxy.service.webview.monitor.work.request.bean

/**
 * @author: cangHX
 * @date: 2026/6/7
 * @desc: 请求开始事件数据，由 JS 在 XHR send / fetch 调用时上报。
 */
class RequestStartData {

    /**
     * 与完成态 RequestData.requestId 一致，用于关联 start/end。
     * */
    var requestId: String? = ""

    /**
     * 请求来源，xhr 或 fetch。
     * */
    var requestType: String? = ""

    /**
     * 当前页面地址。
     * */
    var pageUrl: String? = ""

    /**
     * 请求地址。
     * */
    var requestUrl: String? = ""

    /**
     * 请求方法。
     * */
    var method: String? = ""

    /**
     * 请求开始时间，performance.now()。
     * */
    var startTime: Float? = null

    /**
     * 事件阶段，固定为 start。
     * */
    var phase: String? = "start"
}

package com.proxy.service.webserver.base.http

/**
 * Web 控制台 HTTP 请求抽象（与具体 HTTP 引擎解耦）
 */
interface WebServerHttpRequest {

    /** HTTP 方法，如 GET、POST */
    val method: String

    /** 请求 URI（不含 query） */
    val uri: String

    /** 请求头 */
    val headers: Map<String, String>

    /** 查询参数 */
    val queryParams: Map<String, String>

    /**
     * 读取表单请求体字段（application/x-www-form-urlencoded 等）
     */
    fun readFormBody(): Map<String, String>
}

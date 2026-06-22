package com.proxy.service.apihttp.webserver.config

/**
 * HTTP 代理模式
 */
enum class HttpProxyMode(val wireValue: String) {

    /** 放行：转发数据到 Web 控制台，不阻塞请求 */
    PASS_THROUGH("pass"),

    /** 拦截：阻塞等待页面决策，可替换返回值 */
    INTERCEPT("intercept");

    companion object {

        fun fromWire(value: String?): HttpProxyMode {
            return values().firstOrNull { it.wireValue == value } ?: PASS_THROUGH
        }
    }
}

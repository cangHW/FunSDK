package com.proxy.service.webserver.base.constants

/**
 * 路由层统一错误文案
 */
object RouteErrors {

    fun staticNotMatched(uri: String): String = "static route not matched: $uri"

    fun pluginNotMatched(uri: String): String = "plugin route not matched: $uri"

    fun customNotMatched(uri: String): String = "custom route not matched: $uri"

    fun notMatched(uri: String): String = "route not matched: $uri"
}

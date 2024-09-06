package com.proxy.service.webview.base.web.permissions

/**
 * @author: cangHX
 * @data: 2024/8/5 10:38
 * @desc:
 */
interface GeolocationPermissionsCallback {
    /**
     * @param origin    这是需要设置权限的网页来源（通常是一个URL）
     * @param allow     是否允许该来源使用地理位置API
     * @param retain    权限是否应在当前WebView显示页面的生命周期之外保留
     * */
    fun invoke(origin: String, allow: Boolean, retain: Boolean)
}
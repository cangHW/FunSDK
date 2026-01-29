package com.proxy.service.webview.base

import com.proxy.service.base.BaseService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader
import com.proxy.service.webview.base.web.callback.ValueCallback

/**
 * @author: cangHX
 * @data: 2024/7/31 18:27
 * @desc:
 */
interface WebService : BaseService {

    /**
     * 根据 web 容器配置创建 web 加载器
     * */
    fun createWebLoader(config: WebConfig): IWebLoader

    /**
     * 设置 bridge，允许设置多个，最终合并到同一默认命名空间（_mBridge）
     * */
    fun addGlobalJavascriptInterface(any: Any)

    /**
     * 设置 bridge，允许设置多个，如果命名空间名称相同，则合并
     * */
    fun addGlobalJavascriptInterface(nameSpace: String, any: Any)

    /**
     * 是否接受所有 Cookie（包括第一方和第三方 Cookie）
     * */
    fun setAcceptGlobalCookie(accept: Boolean)

    /**
     * 获取指定 URL 的 Cookie（多个 Cookie 用分号分隔）
     * */
    fun getGlobalCookie(url: String): String

    /**
     * 设置全局 cookie
     * */
    fun setGlobalCookie(url: String, value: String, callback: ValueCallback<Boolean>? = null)

    /**
     * 清除特定域名的 Cookie
     * */
    fun removeGlobalCookie(url: String, callback: ValueCallback<Boolean>? = null)

    /**
     * 清除所有 Cookie
     * */
    fun removeAllGlobalCookies(callback: ValueCallback<Boolean>? = null)

    /**
     * 使设置的全局 cookie 立即生效
     * */
    fun flushGlobalCookie()
}
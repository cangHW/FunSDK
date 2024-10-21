package com.proxy.service.core.service.web

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader

/**
 * Web 容器框架入口
 *
 * @author: cangHX
 * @data: 2024/8/5 14:48
 * @desc:
 */
object CsWeb {

    private const val TAG = "${Constants.TAG}Web"

    private var service: WebService? = null

    private fun getService(): WebService? {
        if (service == null) {
            service = CloudSystem.getService(WebService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG).e("Please check to see if it is referenced. <io.github.cangHW:Service-Webview:xxx>")
        }
        return service
    }

    private val defaultConfig = WebConfig.builder().build()
    private var appConfig: WebConfig? = null

    /**
     * 设置 web 容器默认配置
     * */
    fun setWebConfig(config: WebConfig) {
        appConfig = config
    }

    /**
     * 根据 web 容器配置创建 web 加载器
     * */
    fun createWebLoader(): IWebLoader? {
        val service = getService()
        appConfig?.let {
            return service?.createWebLoader(it)
        }
        return service?.createWebLoader(defaultConfig)
    }

    /**
     * 根据 web 容器配置创建 web 加载器
     * */
    fun createWebLoader(config: WebConfig): IWebLoader? {
        return getService()?.createWebLoader(config)
    }

    /**
     * 设置 bridge，允许设置多个，最终合并到同一默认命名空间（_android）
     * */
    fun addGlobalJavascriptInterface(any: Any) {
        getService()?.addGlobalJavascriptInterface(any)
    }

    /**
     * 设置 bridge，允许设置多个，如果命名空间名称相同，则合并
     * */
    fun addGlobalJavascriptInterface(nameSpace: String, any: Any) {
        getService()?.addGlobalJavascriptInterface(nameSpace, any)
    }

    /**
     * 设置全局 cookie
     * */
    fun setGlobalCookie(url: String, value: String) {
        getService()?.setGlobalCookie(url, value)
    }

    /**
     * 使设置的全局 cookie 立即生效
     * */
    fun flushGlobalCookie() {
        getService()?.flushGlobalCookie()
    }

}
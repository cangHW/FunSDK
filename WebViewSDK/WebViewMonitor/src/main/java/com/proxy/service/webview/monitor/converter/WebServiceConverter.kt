package com.proxy.service.webview.monitor.converter

import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.web.IWebLoader

/**
 * @author: cangHX
 * @date: 2026/1/23 13:29
 * @desc:
 */
class WebServiceConverter(
    private val service: WebService
) : WebService by service {

    override fun createWebLoader(config: WebConfig): IWebLoader {
        return WebLoaderConverter(service.createWebLoader(config))
    }

}
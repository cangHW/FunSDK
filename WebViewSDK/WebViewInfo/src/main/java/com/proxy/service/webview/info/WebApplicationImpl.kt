package com.proxy.service.webview.info

import android.app.Application
import android.webkit.WebView
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.webview.info.config.Config


/**
 * @author: cangHX
 * @data: 2024/8/1 16:37
 * @desc:
 */
@CloudApiService(serviceTag = "application/web")
class WebApplicationImpl : CsBaseApplication() {

    override fun onCreate(application: Application, isDebug: Boolean) {
        super.onCreate(application, isDebug)
        Config.isDebug = isDebug

        WebView.setWebContentsDebuggingEnabled(isDebug)
    }

}
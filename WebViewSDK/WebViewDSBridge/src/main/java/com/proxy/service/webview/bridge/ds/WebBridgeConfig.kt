package com.proxy.service.webview.bridge.ds

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.bridge.ds.converter.WebServiceConverter

/**
 * @author: cangHX
 * @data: 2024/8/10 15:20
 * @desc:
 */
@CloudApiService(serviceTag = "web_bridge/application")
class WebBridgeConfig : CsBaseConfig() {

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    override fun priority(): Int {
        return -550
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CloudSystem.addConverter(WebService::class.java) { t ->
            WebServiceConverter(t)
        }
    }

}
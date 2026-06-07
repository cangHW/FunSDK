package com.proxy.service.webview.monitor

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.webview.base.WebService
import com.proxy.service.webview.monitor.converter.WebServiceConverter

/**
 * @author: cangHX
 * @date: 2024/8/10 15:20
 * @desc:
 */
@CloudApiService(serviceTag = "${CoreConfig.SERVICE_TAG_CONFIG_PREFIX}/web_monitor")
class WebMonitorConfig : CsBaseConfig() {

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return CoreConfig.PRIORITY_CONFIG_WEB_MONITOR
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CloudSystem.addConverter(WebService::class.java) { t ->
            WebServiceConverter(t)
        }
    }

}
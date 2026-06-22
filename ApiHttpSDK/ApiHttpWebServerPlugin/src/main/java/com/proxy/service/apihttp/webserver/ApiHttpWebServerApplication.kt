package com.proxy.service.apihttp.webserver

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.webserver.plugin.ApiHttpWebServerPlugin
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.service.webserver.CsWebServer

/**
 * @author: cangHX
 * @date: 2026/6/21 17:15
 * @desc:
 */
@CloudApiService(serviceTag = "${CoreConfig.SERVICE_TAG_APP_PREFIX}/ApiHttpWebServer")
class ApiHttpWebServerApplication: CsBaseApplication() {
    override fun onCreate(application: Application, isDebug: Boolean) {
        CsWebServer.start(ApiHttpWebServerPlugin.PLUGIN_ID)
    }
}
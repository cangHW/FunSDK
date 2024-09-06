package com.proxy.service.apihttp.info

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.core.application.base.CsBaseApplication

/**
 * @author: cangHX
 * @data: 2024/5/22 20:53
 * @desc:
 */
@CloudApiService(serviceTag = "ApiHttp/Application")
class ApiHttpApplication: CsBaseApplication() {

    override fun priority(): Int {
        return -100
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        super.onCreate(application, isDebug)
        Config.setIsDebug(isDebug)
    }


}
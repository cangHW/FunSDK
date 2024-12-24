package com.proxy.service.core.application

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.framework.app.context.ContextInit

/**
 * @author: cangHX
 * @data: 2024/4/28 20:53
 * @desc:
 */
@CloudApiService(serviceTag = "Core/Config")
class CoreFrameworkConfig : CsBaseConfig() {

    override fun priority(): Int {
        return -999
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        ContextInit.init(application)
    }

}
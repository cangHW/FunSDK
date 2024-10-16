package com.proxy.service.core.application

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.app.context.ContextInit
import com.proxy.service.core.framework.io.sp.SpInit
import com.proxy.service.core.framework.data.log.LogInit

/**
 * @author: cangHX
 * @data: 2024/4/28 20:53
 * @desc:
 */
@CloudApiService(serviceTag = "Core/Application")
class CoreApplication : CsBaseApplication() {

    override fun onCreate(application: Application, isDebug: Boolean) {
        LogInit.setIsDebug(isDebug)
        super.onCreate(application, isDebug)
        ContextInit.init(application)
        SpInit.init(application)
    }

}
package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.Arrays

/**
 * @author: cangHX
 * @data: 2025/9/28 09:31
 * @desc:
 */
@CloudApiService(serviceTag = "Application3")
class Application3: CsBaseApplication() {

    override fun dependsOn(): List<Class<out CsBaseApplication>> {
        return Arrays.asList(
            Application1::class.java
        )
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CsLogger.tag("CsBaseApplication").d("Application3")
    }
}
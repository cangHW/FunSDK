package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/9/28 09:31
 * @desc:
 */
@CloudApiService(serviceTag = "Application1")
class Application1: CsBaseApplication() {

    override fun runOnThread(): ThreadType {
        return ThreadType.IO_THREAD
    }

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    override fun priority(): Int {
        return -1
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CsLogger.tag("CsBaseApplication").d("Application1")
    }
}
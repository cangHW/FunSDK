package com.proxy.service.threadpool.info

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.threadpool.info.constants.Constants
import io.reactivex.plugins.RxJavaPlugins

/**
 * @author: cangHX
 * @data: 2024/9/14 16:27
 * @desc:
 */
@CloudApiService(serviceTag = "application/thread_pool")
class ThreadPoolApplication: CsBaseApplication() {

    override fun onCreate(application: Application, isDebug: Boolean) {
        super.onCreate(application, isDebug)
        RxJavaPlugins.setErrorHandler { throwable->
            CsLogger.tag(Constants.TAG).e(throwable)
        }
    }

}
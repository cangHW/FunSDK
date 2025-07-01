package com.proxy.service.threadpool.info

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.constants.ThreadConstants
import io.reactivex.plugins.RxJavaPlugins

/**
 * @author: cangHX
 * @data: 2024/9/14 16:27
 * @desc:
 */
@CloudApiService(serviceTag = "config/thread_pool")
class ThreadPoolConfig : CsBaseConfig() {

    override fun priority(): Int {
        return -900
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        RxJavaPlugins.setErrorHandler { throwable ->
            CsLogger.tag(ThreadConstants.TAG).e(throwable)
        }
    }

}
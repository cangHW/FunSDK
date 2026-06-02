package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.apm.info.cache.ExceptionHandler
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.performance.mainthread.MainThreadLagConfig
import com.proxy.service.core.CsCore
import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.monitor.info.FileInfo

/**
 * @author: cangHX
 * @date: 2024/12/19 11:12
 * @desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        val config = LogConfig.builder()
//            .setFlushEveryTime(5, TimeUnit.SECONDS)
//            .setCompressionMode(CompressionMode.LZ4)
//            .setEncryptionMode(EncryptionMode.CHACHA20, "111222")
//            .createDailyType(0, 0)
//        CsLogFile.setConfig(config)

        CsCore.init(this, BuildConfig.DEBUG)
//        CsCore.init(this, false)
    }

}
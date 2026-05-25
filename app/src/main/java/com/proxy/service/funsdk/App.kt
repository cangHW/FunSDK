package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.performance.mainthread.MainThreadLagConfig
import com.proxy.service.core.CsCore

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

        initApm()
        CsCore.init(this, BuildConfig.DEBUG)
//        CsCore.init(this, false)
    }

    private fun initApm(){
        val mtl = MainThreadLagConfig.builder()
            .setEnable(true)
            .build()

        val apmConfig = ApmConfig.builder()
            .setMainThreadLagMonitorConfig(mtl)
            .build()

        CsApmMonitor.setConfig(apmConfig)
    }

}
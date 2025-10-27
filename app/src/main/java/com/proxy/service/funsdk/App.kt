package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.core.CsCore
import com.proxy.service.logfile.info.CsLogFile
import com.proxy.service.logfile.info.config.LogConfig
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/19 11:12
 * @desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = LogConfig.builder()
            .setFlushEveryTime(5, TimeUnit.SECONDS)
            .createDailyType(0, 0)
        CsLogFile.setConfig(config)
        CsCore.init(this, BuildConfig.DEBUG)
    }

}
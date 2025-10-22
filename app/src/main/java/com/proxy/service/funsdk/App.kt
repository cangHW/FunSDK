package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.core.CsCore
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.logfile.info.CsLogFile
import com.proxy.service.logfile.info.config.LogConfig
import com.proxy.service.logfile.info.manager.CompressionMode
import com.proxy.service.threadpool.base.thread.task.ICallable
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
            .setCacheTime(1, TimeUnit.SECONDS)
//            .setFlushEveryTime(5, TimeUnit.SECONDS)
            .setCompressionMode(CompressionMode.LZ4)
            .createNormalType()
//            .createDailyType(0,0)
        CsLogFile.setConfig(config)
        CsCore.init(this, true)
    }

}
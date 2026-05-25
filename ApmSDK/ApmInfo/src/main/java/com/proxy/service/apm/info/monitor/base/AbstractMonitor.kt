package com.proxy.service.apm.info.monitor.base

import android.app.Application
import com.proxy.service.apm.info.cache.CacheManager
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.base.IBaseConfigGet
import com.proxy.service.apm.info.config.controller.base.IConfigGet
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @date: 2026/5/22 15:07
 * @desc:
 */
abstract class AbstractMonitor<T : IConfigGet> {

    private val isInit = AtomicBoolean(false)

    fun init(application: Application, apmConfig: ApmConfig, config: T) {
        if (!config.getEnable()) {
            return
        }
        if (!isInit.compareAndSet(false, true)) {
            return
        }
        getLogFileDir(application)?.let {
            if (config is IBaseConfigGet) {
                CacheManager.getInstance().startWatch(it, config)
            }
        }
        start(application, apmConfig, config)
    }

    protected abstract fun start(application: Application, apmConfig: ApmConfig, config: T)

    open fun stop() {
        isInit.set(false)
    }

    protected abstract fun getLogFileDir(application: Application): String?

}
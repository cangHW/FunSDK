package com.proxy.service.core.framework.app.config.controller

import android.content.Context
import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.base.BaseConfigurationCheck

/**
 * @author: cangHX
 * @data: 2024/12/25 10:05
 * @desc:
 */
object ConfigurationManager {

    /**
     * 系统配置变化
     * */
    fun systemConfigurationChange(newConfig: Configuration) {
        BaseConfigurationCheck.getAllConfigurations().forEach {
            it.onSystemConfigurationChanged(newConfig)
        }
    }

    /**
     * 更新配置
     * */
    fun applyConfiguration(context: Context) {
        changeConfiguration(context)
        val appContext = context.applicationContext
        if (appContext != context) {
            changeConfiguration(appContext)
        }
    }

    private fun changeConfiguration(context: Context) {
        val res = context.resources
        val config = Configuration(res.configuration)

        var shouldUpdate = false

        BaseConfigurationCheck.getAllConfigurations().forEach {
            if (it.onConfigurationCheck(context, config)) {
                shouldUpdate = true
            }
        }

        if (shouldUpdate) {
            res.updateConfiguration(config, res.displayMetrics)
        }
    }
}
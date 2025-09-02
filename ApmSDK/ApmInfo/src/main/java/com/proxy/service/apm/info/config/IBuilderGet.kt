package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.MonitorConfig

/**
 * @author: cangHX
 * @data: 2025/4/22 17:40
 * @desc:
 */
interface IBuilderGet {

    fun getRootDir(): String

    fun getJavaCrashMonitorConfig(): MonitorConfig

    fun getMainThreadLagMonitorConfig(): MonitorConfig

    fun getUiLagMonitorConfig(): MonitorConfig

}
package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.Controller

/**
 * @author: cangHX
 * @data: 2025/4/22 17:40
 * @desc:
 */
interface IBuilderGet {

    fun getRootDir(): String

    fun getJavaCrashMonitorController(): Controller

    fun getMainThreadLagMonitorController(): Controller

    fun getUiLagMonitorController(): Controller

}
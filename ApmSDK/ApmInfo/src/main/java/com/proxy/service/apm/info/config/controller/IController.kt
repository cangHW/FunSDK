package com.proxy.service.apm.info.config.controller

import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/4/22 18:13
 * @desc:
 */
interface IController {

    fun setEnable(enable: Boolean): IController

    fun setMaxFileCount(maxFileCount: Int): IController

    fun setAllFilesMaxSize(maxSize: Long): IController

    fun setMaxCacheTime(time: Long, unit: TimeUnit): IController

    fun build(): Controller
}
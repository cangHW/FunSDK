package com.proxy.service.apm.info.config.controller

/**
 * @author: cangHX
 * @data: 2025/4/22 18:13
 * @desc:
 */
interface IMonitorConfigGet {

    fun getEnable(): Boolean

    fun getMaxFileCount(): Int

    fun getAllFilesMaxSize(): Long

    fun getMaxCacheTime(): Long

}
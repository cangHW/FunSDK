package com.proxy.service.apm.info.config.controller

import com.proxy.service.apm.info.constants.Constants
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/4/22 18:13
 * @desc:
 */
interface IMonitorConfig {

    /**
     * 设置功能是否开启, 默认值: [Constants.FUN_ENABLE]
     * */
    fun setEnable(enable: Boolean): IMonitorConfig

    /**
     * 设置最大文件数量, 默认值: [Constants.MAX_FILE_COUNT]
     * */
    fun setMaxFileCount(maxFileCount: Int): IMonitorConfig

    /**
     * 设置最大占用存储, 默认值: [Constants.MAX_ALL_FILE_SIZE]
     * */
    fun setAllFilesMaxSize(maxSize: Long): IMonitorConfig

    /**
     * 设置保存最长时间, 默认值: [Constants.CACHE_TIME]
     * */
    fun setMaxCacheTime(time: Long, unit: TimeUnit): IMonitorConfig

    /**
     * 构建
     * */
    fun build(): MonitorConfig
}
package com.proxy.service.apm.info.config.controller.base

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.convert.CsStorageUnit
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2025/4/22 18:13
 * @desc:
 */
interface IBaseConfig<T> : IConfig<T> {

    /**
     * 设置最大文件数量, 默认值 [Constants.MONITOR_COMMON_MAX_FILE_COUNT]
     * */
    fun setMaxFileCount(maxFileCount: Int): T

    /**
     * 设置最大占用存储, 默认值 [Constants.MONITOR_COMMON_MAX_ALL_FILE_SIZE_BYTE]
     * */
    fun setAllFilesMaxSize(maxSize: Long, unit: CsStorageUnit): T

    /**
     * 设置保存最长时间, 默认值 [Constants.MONITOR_COMMON_CACHE_TIME]
     * */
    fun setMaxCacheTime(time: Long, unit: TimeUnit): T

}
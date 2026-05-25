package com.proxy.service.apm.info.config.controller.base

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.convert.CsStorageUnit
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2026/5/22 14:29
 * @desc:
 */
abstract class BaseConfig<T> : Config<T>(), IBaseConfig<T>, IBaseConfigGet {

    private var maxFileCount: Int = Constants.MONITOR_COMMON_MAX_FILE_COUNT
    private var maxSize: Long = Constants.MONITOR_COMMON_MAX_ALL_FILE_SIZE_BYTE
    private var maxCacheTime: Long = Constants.MONITOR_COMMON_CACHE_TIME

    override fun setMaxFileCount(maxFileCount: Int): T {
        if (maxFileCount > 0) {
            this.maxFileCount = maxFileCount
        }
        return getInstance()
    }

    override fun setAllFilesMaxSize(maxSize: Long, unit: CsStorageUnit): T {
        if (maxSize > 0) {
            this.maxSize = unit.toBLong(maxSize)
        }
        return getInstance()
    }

    override fun setMaxCacheTime(time: Long, unit: TimeUnit): T {
        if (time > 0) {
            this.maxCacheTime = unit.toMillis(time)
        }
        return getInstance()
    }

    override fun getMaxFileCount(): Int {
        return maxFileCount
    }

    override fun getAllFilesMaxSize(): Long {
        return maxSize
    }

    override fun getMaxCacheTime(): Long {
        return maxCacheTime
    }
}
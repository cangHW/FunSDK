package com.proxy.service.apm.info.config.controller

import com.proxy.service.apm.info.constants.Constants
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/4/22 18:33
 * @desc:
 */
class Controller private constructor(private val controller: IControllerGet) : IControllerGet {
    override fun getEnable(): Boolean {
        return controller.getEnable()
    }

    override fun getMaxFileCount(): Int {
        return controller.getMaxFileCount()
    }

    override fun getAllFilesMaxSize(): Long {
        return controller.getAllFilesMaxSize()
    }

    override fun getMaxCacheTime(): Long {
        return controller.getMaxCacheTime()
    }

    companion object {
        fun builder(): IController {
            return Builder()
        }
    }

    class Builder : IController, IControllerGet {

        private var enable: Boolean = true
        private var maxFileCount: Int = Constants.MAX_FILE_COUNT
        private var maxSize: Long = Constants.MAX_ALL_FILE_SIZE
        private var maxCacheTime: Long = Constants.CACHE_TIME

        override fun setEnable(enable: Boolean): IController {
            this.enable = enable
            return this
        }

        override fun setMaxFileCount(maxFileCount: Int): IController {
            this.maxFileCount = maxFileCount
            return this
        }

        override fun setAllFilesMaxSize(maxSize: Long): IController {
            this.maxSize = maxSize
            return this
        }

        override fun setMaxCacheTime(time: Long, unit: TimeUnit): IController {
            this.maxCacheTime = unit.toMillis(time)
            return this
        }

        override fun build(): Controller {
            return Controller(this)
        }

        override fun getEnable(): Boolean {
            return enable
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
}
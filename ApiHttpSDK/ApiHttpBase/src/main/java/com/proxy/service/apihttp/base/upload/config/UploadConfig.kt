package com.proxy.service.apihttp.base.upload.config

import com.proxy.service.apihttp.base.common.config.BaseConfig
import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.upload.config.builder.IUploadConfigBuilder
import com.proxy.service.apihttp.base.upload.config.builder.IUploadConfigBuilderGet
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2024/12/17 15:20
 * @desc:
 */
sealed interface UploadConfig : IUploadConfigBuilderGet {

    companion object {
        private const val TAG = "${ApiConstants.LOG_DOWNLOAD_TAG_START}UploadConfig"

        fun builder(): IUploadConfigBuilder {
            return Builder()
        }
    }

    fun newBuilder(): IUploadConfigBuilder {
        val builder = Builder()
        builder.copyFrom(this)
        return builder
    }

    private class Builder : BaseConfig<IUploadConfigBuilder>(), IUploadConfigBuilder,
        UploadConfig {

        companion object {
            private const val TIMEOUT_MIN: Long = 5 * 1000
        }

        private var connectTimeOut: Long = 10 * 1000

        private var maxTask = 3

        override fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IUploadConfigBuilder {
            unit.toMillis(timeout).let {
                connectTimeOut = if (it > TIMEOUT_MIN) {
                    it
                } else {
                    TIMEOUT_MIN
                }
            }
            return this
        }

        override fun setMaxTask(maxTasks: Int): IUploadConfigBuilder {
            if (maxTasks <= 0 || maxTasks > 5) {
                CsLogger.tag(TAG)
                    .e("maxTasks must be greater than 0 and less than or equal to 5. maxTask = $maxTask")
            } else {
                this.maxTask = maxTasks
            }
            return this
        }

        override fun build(): UploadConfig {
            return this
        }

        override fun getConnectTimeOut(): Long {
            return connectTimeOut
        }

        override fun getMaxTask(): Int {
            return maxTask
        }

        override fun getInstance(): IUploadConfigBuilder {
            return this
        }

        override fun copyFrom(any: Any) {
            super.copyFrom(any)

            if (any is Builder) {
                connectTimeOut = any.getConnectTimeOut()
                maxTask = any.getMaxTask()
            }
        }
    }
}
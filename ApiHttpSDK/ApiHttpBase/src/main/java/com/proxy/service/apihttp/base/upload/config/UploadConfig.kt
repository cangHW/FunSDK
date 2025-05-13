package com.proxy.service.apihttp.base.upload.config

import com.proxy.service.apihttp.base.common.config.BaseConfig
import com.proxy.service.apihttp.base.common.config.BaseConfigGet
import com.proxy.service.apihttp.base.upload.config.builder.IUploadConfigBuilder
import com.proxy.service.apihttp.base.upload.config.builder.IUploadConfigBuilderGet
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/17 15:20
 * @desc:
 */
class UploadConfig private constructor(
    private val builder: IUploadConfigBuilderGet
) : BaseConfigGet(builder), IUploadConfigBuilderGet {

    override fun getConnectTimeOut(): Long {
        return builder.getConnectTimeOut()
    }

    override fun getMaxTask(): Int {
        return builder.getMaxTask()
    }

    companion object {

        fun builder(): IUploadConfigBuilder {
            return Builder()
        }

    }

    private class Builder : BaseConfig<IUploadConfigBuilder>(), IUploadConfigBuilder,
        IUploadConfigBuilderGet {

        private var connectTimeOut: Long = 10 * 1000

        private var maxTasks = 3

        override fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IUploadConfigBuilder {
            this.connectTimeOut = unit.toMillis(timeout)
            return this
        }

        override fun setMaxTask(maxTasks: Int): IUploadConfigBuilder {
            this.maxTasks = maxTasks
            return this
        }

        override fun build(): UploadConfig {
            return UploadConfig(this)
        }

        override fun getConnectTimeOut(): Long {
            return connectTimeOut
        }

        override fun getMaxTask(): Int {
            return maxTasks
        }

        override fun getInstance(): IUploadConfigBuilder {
            return this
        }
    }
}
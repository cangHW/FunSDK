package com.proxy.service.apihttp.base.download.config

import com.proxy.service.apihttp.base.common.config.BaseConfig
import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.download.config.builder.IDownloadConfigBuilder
import com.proxy.service.apihttp.base.download.config.builder.IDownloadConfigBuilderGet
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @date: 2024/10/30 18:40
 * @desc:
 */
sealed interface DownloadConfig : IDownloadConfigBuilderGet {

    companion object {
        private const val TAG = "${ApiConstants.LOG_DOWNLOAD_TAG_START}DownloadConfig"

        fun builder(): IDownloadConfigBuilder {
            return Builder()
        }
    }

    fun newBuilder(): IDownloadConfigBuilder {
        val builder = Builder()
        builder.copyFrom(this)
        return builder
    }

    private class Builder : BaseConfig<IDownloadConfigBuilder>(), IDownloadConfigBuilder,
        DownloadConfig {

        private val groups = ArrayList<DownloadGroup>()
        private var maxTaskNum: Int = ApiConstants.Download.MAX_TASK_NUM
        private var isAutoRestartOnNetworkReconnect = false

        private var connectTimeOut: Long = 10 * 1000

        /**
         * 添加组信息
         * */
        override fun addGroup(group: DownloadGroup): IDownloadConfigBuilder {
            if (group.groupName.trim().isEmpty()) {
                CsLogger.tag(TAG)
                    .e("groupName cannot be empty or blank. groupName = ${group.groupName}")
            } else {
                this.groups.add(group)
            }
            return this
        }

        /**
         * 设置最大同时下载任务数量
         * */
        override fun setMaxTaskCount(maxTaskNum: Int): IDownloadConfigBuilder {
            if (maxTaskNum <= 0 || maxTaskNum > 5) {
                CsLogger.tag(TAG)
                    .e("maxTasks must be greater than 0 and less than or equal to 5. maxTaskNum = $maxTaskNum")
            } else {
                this.maxTaskNum = maxTaskNum
            }
            return this
        }

        /**
         * 设置是否允许网络连接恢复时自动重新启动失败的下载任务
         * */
        override fun setAutoRestartOnNetworkReconnect(enable: Boolean): IDownloadConfigBuilder {
            this.isAutoRestartOnNetworkReconnect = enable
            return this
        }

        override fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IDownloadConfigBuilder {
            unit.toMillis(timeout).let {
                connectTimeOut = if (it > ApiConstants.DEFAULT_TIMEOUT_MIN) {
                    it
                } else {
                    ApiConstants.DEFAULT_TIMEOUT_MIN
                }
            }
            return this
        }

        override fun build(): DownloadConfig {
            return this
        }

        /**
         * 获取组信息
         * */
        override fun getGroups(): MutableList<DownloadGroup> {
            return groups
        }

        /**
         * 获取最大同时下载任务数量
         * */
        override fun getMaxTaskCount(): Int {
            return maxTaskNum
        }

        /**
         * 获取是否允许网络连接恢复时自动重新启动失败的下载任务
         * */
        override fun getAutoRestartOnNetworkReconnect(): Boolean {
            return isAutoRestartOnNetworkReconnect
        }

        override fun getConnectTimeOut(): Long {
            return connectTimeOut
        }

        override fun getInstance(): IDownloadConfigBuilder {
            return this
        }


        override fun copyFrom(any: Any) {
            super.copyFrom(any)

            if (any is Builder) {
                groups.addAll(any.getGroups())
                maxTaskNum = any.maxTaskNum
                isAutoRestartOnNetworkReconnect = any.getAutoRestartOnNetworkReconnect()
                connectTimeOut = any.getConnectTimeOut()
            }
        }

    }

}
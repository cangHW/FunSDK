package com.proxy.service.apihttp.base.download.config

import com.proxy.service.apihttp.base.common.config.BaseConfig
import com.proxy.service.apihttp.base.common.config.BaseConfigGet
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.config.builder.IDownloadConfigBuilder
import com.proxy.service.apihttp.base.download.config.builder.IDownloadConfigBuilderGet
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/10/30 18:40
 * @desc:
 */
class DownloadConfig private constructor(
    private val builder: IDownloadConfigBuilderGet
) : BaseConfigGet(builder), IDownloadConfigBuilderGet {

    /**
     * 获取组信息
     * */
    override fun getGroups(): ArrayList<DownloadGroup> {
        return builder.getGroups()
    }

    /**
     * 获取最大同时下载任务数量
     * */
    override fun getMaxTask(): Int {
        return builder.getMaxTask()
    }

    /**
     * 获取是否允许网络连接恢复时自动重新启动失败的下载任务
     * */
    override fun getAutoRestartOnNetworkReconnect(): Boolean {
        return builder.getAutoRestartOnNetworkReconnect()
    }

    override fun getConnectTimeOut(): Long {
        return builder.getConnectTimeOut()
    }

    companion object {
        private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}DownloadConfig"

        fun builder(): IDownloadConfigBuilder {
            return Builder()
        }
    }

    class Builder : BaseConfig<IDownloadConfigBuilder>(), IDownloadConfigBuilder,
        IDownloadConfigBuilderGet {

        companion object {
            private const val TIMEOUT_MIN: Long = 5 * 1000
        }

        private val groups = ArrayList<DownloadGroup>()
        private var maxTask: Int = 3
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
         * 设置最大同时下载任务数量, 默认为：3
         * */
        override fun setMaxTask(maxTasks: Int): IDownloadConfigBuilder {
            if (maxTasks <= 0 || maxTasks > 5) {
                CsLogger.tag(TAG)
                    .e("maxTasks must be greater than 0 and less than or equal to 5. maxTask = $maxTask")
            } else {
                this.maxTask = maxTasks
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
                connectTimeOut =
                    if (it > TIMEOUT_MIN) {
                        it
                    } else {
                        TIMEOUT_MIN
                    }
            }
            return this
        }

        override fun build(): DownloadConfig {
            return DownloadConfig(this)
        }

        /**
         * 获取组信息
         * */
        override fun getGroups(): ArrayList<DownloadGroup> {
            return groups
        }

        /**
         * 获取最大同时下载任务数量
         * */
        override fun getMaxTask(): Int {
            return maxTask
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

    }

}
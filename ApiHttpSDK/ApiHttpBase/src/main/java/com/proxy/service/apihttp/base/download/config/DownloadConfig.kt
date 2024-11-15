package com.proxy.service.apihttp.base.download.config

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.config.base.IConfigBuilder
import com.proxy.service.apihttp.base.download.config.base.IConfigBuilderGet
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2024/10/30 18:40
 * @desc:
 */
class DownloadConfig private constructor(private val builder: IConfigBuilderGet) :
    IConfigBuilderGet {

    companion object {
        private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}DownloadConfig"

        fun builder(): IConfigBuilder {
            return Builder()
        }
    }

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

    /**
     * 获取是否允许应用重启时自动恢复未完成的下载任务
     * */
    override fun getAutoResumeOnAppRelaunch(): Boolean {
        return builder.getAutoResumeOnAppRelaunch()
    }

    class Builder : IConfigBuilder, IConfigBuilderGet {

        private val groups = ArrayList<DownloadGroup>()
        private var maxTask: Int = 3
        private var isAutoRestartOnNetworkReconnect = false
        private var isAutoResumeOnAppRelaunch = false

        /**
         * 添加组信息
         * */
        override fun addGroup(group: DownloadGroup): Builder {
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
        override fun setMaxTask(maxTask: Int): Builder {
            if (maxTask <= 0 || maxTask > 5) {
                CsLogger.tag(TAG)
                    .e("maxTask must be greater than 0 and less than or equal to 5. maxTask = $maxTask")
            } else {
                this.maxTask = maxTask
            }
            return this
        }

        /**
         * 设置是否允许网络连接恢复时自动重新启动失败的下载任务
         * */
        override fun setAutoRestartOnNetworkReconnect(enable: Boolean): IConfigBuilder {
            this.isAutoRestartOnNetworkReconnect = enable
            return this
        }

        /**
         * 设置是否允许应用重启时自动恢复未完成的下载任务
         * */
        override fun setAutoResumeOnAppRelaunch(enable: Boolean): IConfigBuilder {
            this.isAutoResumeOnAppRelaunch = enable
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

        /**
         * 获取是否允许应用重启时自动恢复未完成的下载任务
         * */
        override fun getAutoResumeOnAppRelaunch(): Boolean {
            return isAutoResumeOnAppRelaunch
        }

    }

}
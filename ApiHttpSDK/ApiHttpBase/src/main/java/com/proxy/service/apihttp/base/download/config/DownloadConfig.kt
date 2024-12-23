package com.proxy.service.apihttp.base.download.config

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.config.builder.IDownloadConfigBuilder
import com.proxy.service.apihttp.base.download.config.builder.IDownloadConfigBuilderGet
import com.proxy.service.core.framework.data.log.CsLogger
import okhttp3.Dns
import okhttp3.EventListener
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/10/30 18:40
 * @desc:
 */
class DownloadConfig private constructor(
    private val builder: IDownloadConfigBuilderGet
) : IDownloadConfigBuilderGet {

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

    override fun getConnectTimeOut(): Long {
        return builder.getConnectTimeOut()
    }

    override fun getInterceptor(): MutableList<Interceptor> {
        return builder.getInterceptor()
    }

    override fun getNetworkInterceptor(): MutableList<Interceptor> {
        return builder.getNetworkInterceptor()
    }

    override fun getEventListener(): EventListener? {
        return builder.getEventListener()
    }

    override fun getDns(): Dns? {
        return builder.getDns()
    }

    override fun getServerCerAssetsName(): String? {
        return builder.getServerCerAssetsName()
    }

    override fun getClientCerAssetsName(): String? {
        return builder.getClientCerAssetsName()
    }

    override fun getClientCerPassWord(): String? {
        return builder.getClientCerPassWord()
    }

    companion object {
        private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}DownloadConfig"

        fun builder(): IDownloadConfigBuilder {
            return Builder()
        }
    }

    class Builder : IDownloadConfigBuilder, IDownloadConfigBuilderGet {

        companion object {
            private const val TIMEOUT_MIN: Long = 5 * 1000
        }

        private val groups = ArrayList<DownloadGroup>()
        private var maxTask: Int = 3
        private var isAutoRestartOnNetworkReconnect = false
        private var isAutoResumeOnAppRelaunch = false

        private var connectTimeOut: Long = 10 * 1000
        private var interceptors: MutableList<Interceptor> = ArrayList()
        private var networkInterceptors: MutableList<Interceptor> = ArrayList()
        private var eventListener: EventListener? = null
        private var dns: Dns? = null

        private var serverCerAssetsName: String? = null
        private var clientCerAssetsName: String? = null
        private var clientCerPassWord: String? = null

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
        override fun setMaxTask(maxTasks: Int): Builder {
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

        /**
         * 设置是否允许应用重启时自动恢复未完成的下载任务
         * */
        override fun setAutoResumeOnAppRelaunch(enable: Boolean): IDownloadConfigBuilder {
            this.isAutoResumeOnAppRelaunch = enable
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

        override fun addInterceptor(interceptor: Interceptor): IDownloadConfigBuilder {
            interceptors.add(interceptor)
            return this
        }

        override fun addNetworkInterceptor(interceptor: Interceptor): IDownloadConfigBuilder {
            networkInterceptors.add(interceptor)
            return this
        }

        override fun setEventListener(eventListener: EventListener): IDownloadConfigBuilder {
            this.eventListener = eventListener
            return this
        }

        override fun setDns(dns: Dns): IDownloadConfigBuilder {
            this.dns = dns
            return this
        }

        override fun setSslSocket(serverCerAssetsName: String): IDownloadConfigBuilder {
            this.serverCerAssetsName = serverCerAssetsName
            this.clientCerAssetsName = null
            this.clientCerPassWord = null
            return this
        }

        override fun setSslSocket(
            serverCerAssetsName: String,
            clientCerAssetsName: String,
            clientCerPassWord: String
        ): IDownloadConfigBuilder {
            this.serverCerAssetsName = serverCerAssetsName
            this.clientCerAssetsName = clientCerAssetsName
            this.clientCerPassWord = clientCerPassWord
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

        override fun getConnectTimeOut(): Long {
            return connectTimeOut
        }

        override fun getInterceptor(): MutableList<Interceptor> {
            return interceptors
        }

        override fun getNetworkInterceptor(): MutableList<Interceptor> {
            return networkInterceptors
        }

        override fun getEventListener(): EventListener? {
            return eventListener
        }

        override fun getDns(): Dns? {
            return dns
        }

        override fun getServerCerAssetsName(): String? {
            return serverCerAssetsName
        }

        override fun getClientCerAssetsName(): String? {
            return clientCerAssetsName
        }

        override fun getClientCerPassWord(): String? {
            return clientCerPassWord
        }

    }

}
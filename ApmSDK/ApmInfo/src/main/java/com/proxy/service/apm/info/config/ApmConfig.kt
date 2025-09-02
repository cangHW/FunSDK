package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.MonitorConfig

/**
 * @author: cangHX
 * @data: 2025/4/22 17:39
 * @desc:
 */
class ApmConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun getRootDir(): String {
        return builder.getRootDir()
    }

    override fun getJavaCrashMonitorConfig(): MonitorConfig {
        return builder.getJavaCrashMonitorConfig()
    }

    override fun getMainThreadLagMonitorConfig(): MonitorConfig {
        return builder.getMainThreadLagMonitorConfig()
    }

    override fun getUiLagMonitorConfig(): MonitorConfig {
        return builder.getUiLagMonitorConfig()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var rootDir = ""
        private var javaCrashMonitorConfig: MonitorConfig = MonitorConfig.builder().build()
        private var mainThreadLagMonitorConfig: MonitorConfig = MonitorConfig.builder().build()
        private var uiLagMonitorConfig: MonitorConfig = MonitorConfig.builder().build()

        override fun setRootDir(rootDir: String): IBuilder {
            this.rootDir = rootDir
            return this
        }

        override fun setJavaCrashMonitorConfig(config: MonitorConfig): IBuilder {
            this.javaCrashMonitorConfig = config
            return this
        }

        override fun setMainThreadLagMonitorConfig(config: MonitorConfig): IBuilder {
            this.mainThreadLagMonitorConfig = config
            return this
        }

        override fun setUiLagMonitorConfig(config: MonitorConfig): IBuilder {
            this.uiLagMonitorConfig = config
            return this
        }

        override fun build(): ApmConfig {
            return ApmConfig(this)
        }

        override fun getRootDir(): String {
            return rootDir
        }

        override fun getJavaCrashMonitorConfig(): MonitorConfig {
            return javaCrashMonitorConfig
        }

        override fun getMainThreadLagMonitorConfig(): MonitorConfig {
            return mainThreadLagMonitorConfig
        }

        override fun getUiLagMonitorConfig(): MonitorConfig {
            return uiLagMonitorConfig
        }
    }

}
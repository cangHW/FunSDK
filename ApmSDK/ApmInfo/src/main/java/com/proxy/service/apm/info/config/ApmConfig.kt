package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.common.CommonConfig
import com.proxy.service.apm.info.config.controller.performance.mainthread.MainThreadLagConfig
import com.proxy.service.apm.info.config.controller.performance.uilag.UiLagConfig
import com.proxy.service.apm.info.monitor.crash.bean.ExceptionReport
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.bean.MainThreadLagReport
import com.proxy.service.apm.info.monitor.performance.lag.ui.bean.UiLagReport
import com.proxy.service.apm.info.report.IReporter

/**
 * ApmInfo 全局配置，通过 [com.proxy.service.apm.info.CsApmMonitor.setConfig] 注入。
 *
 * @author: cangHX
 * @date: 2025/4/22 17:39
 */
class ApmConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet by builder {

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var rootDir = ""

        private var crashMonitorConfig = CommonConfig.builder().build()
        private var javaCrashReporter: IReporter<ExceptionReport>? = null

        private var mainThreadLagMonitorConfig = MainThreadLagConfig.builder().build()
        private var mainThreadLagReporter: IReporter<MainThreadLagReport>? = null

        private var uiLagMonitorConfig = UiLagConfig.builder().build()
        private var uiLagReporter: IReporter<UiLagReport>? = null

        override fun setRootDir(rootDir: String): IBuilder {
            this.rootDir = rootDir
            return this
        }

        override fun setJavaCrashMonitorConfig(
            config: CommonConfig,
            reporter: IReporter<ExceptionReport>?
        ): IBuilder {
            this.crashMonitorConfig = config
            this.javaCrashReporter = reporter
            return this
        }

        override fun setMainThreadLagMonitorConfig(
            config: MainThreadLagConfig,
            reporter: IReporter<MainThreadLagReport>?
        ): IBuilder {
            this.mainThreadLagMonitorConfig = config
            this.mainThreadLagReporter = reporter
            return this
        }

        override fun setUiLagMonitorConfig(
            config: UiLagConfig,
            reporter: IReporter<UiLagReport>?
        ): IBuilder {
            this.uiLagMonitorConfig = config
            this.uiLagReporter = reporter
            return this
        }

        override fun build(): ApmConfig {
            return ApmConfig(this)
        }

        override fun getRootDir(): String {
            return rootDir
        }

        override fun getJavaCrashMonitorConfig(): CommonConfig {
            return crashMonitorConfig
        }

        override fun getJavaCrashReporter(): IReporter<ExceptionReport>? {
            return javaCrashReporter
        }

        override fun getMainThreadLagMonitorConfig(): MainThreadLagConfig {
            return mainThreadLagMonitorConfig
        }

        override fun getMainThreadLagReporter(): IReporter<MainThreadLagReport>? {
            return mainThreadLagReporter
        }

        override fun getUiLagMonitorConfig(): UiLagConfig {
            return uiLagMonitorConfig
        }

        override fun getUiLagReporter(): IReporter<UiLagReport>? {
            return uiLagReporter
        }
    }

}

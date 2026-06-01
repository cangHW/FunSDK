package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.common.CommonConfig
import com.proxy.service.apm.info.config.controller.performance.mainthread.MainThreadLagConfig
import com.proxy.service.apm.info.config.controller.performance.uilag.UiLagConfig
import com.proxy.service.apm.info.monitor.crash.java_crash.report.ExceptionReport
import com.proxy.service.apm.info.monitor.crash.native_crash.report.NativeCrashReport
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.bean.MainThreadLagReport
import com.proxy.service.apm.info.monitor.performance.lag.ui.bean.UiLagReport
import com.proxy.service.apm.info.report.IReporter

/**
 * @author: cangHX
 * @date: 2025/4/22 17:40
 * @desc:
 */
interface IBuilder {

    /**
     * 设置根文件夹路径
     * */
    fun setRootDir(rootDir: String): IBuilder

    /**
     * 设置 java crash 监控配置
     * */
    fun setJavaCrashMonitorConfig(
        config: CommonConfig,
        reporter: IReporter<ExceptionReport>? = null
    ): IBuilder

    /**
     * 设置主线程慢分发监控配置。
     */
    fun setMainThreadLagMonitorConfig(
        config: MainThreadLagConfig,
        reporter: IReporter<MainThreadLagReport>? = null
    ): IBuilder

    /**
     * 设置 native crash 监控配置
     * */
    fun setNativeCrashMonitorConfig(
        config: CommonConfig,
        reporter: IReporter<NativeCrashReport>? = null
    ): IBuilder

    /**
     * 设置 UI 渲染卡顿监控配置
     * */
    fun setUiLagMonitorConfig(
        config: UiLagConfig,
        reporter: IReporter<UiLagReport>? = null
    ): IBuilder

    /**
     * 创建一个配置
     * */
    fun build(): ApmConfig
}
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
interface IBuilderGet {

    fun getRootDir(): String

    /**
     * Crash监控配置。
     */
    fun getJavaCrashMonitorConfig(): CommonConfig

    /**
     * JavaCrash报告业务回调。
     */
    fun getJavaCrashReporter(): IReporter<ExceptionReport>?

    /**
     * NativeCrash监控配置。
     */
    fun getNativeCrashMonitorConfig(): CommonConfig

    /**
     * NativeCrash报告业务回调。
     */
    fun getNativeCrashReporter(): IReporter<NativeCrashReport>?

    /**
     * 主线程慢分发监控配置。
     */
    fun getMainThreadLagMonitorConfig(): MainThreadLagConfig

    /**
     * 主线程慢分发报告业务回调。
     */
    fun getMainThreadLagReporter(): IReporter<MainThreadLagReport>?

    fun getUiLagMonitorConfig(): UiLagConfig

    /**
     * UI渲染卡顿报告业务回调。
     */
    fun getUiLagReporter(): IReporter<UiLagReport>?
}
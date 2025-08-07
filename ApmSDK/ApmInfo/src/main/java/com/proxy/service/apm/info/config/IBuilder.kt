package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.MonitorConfig

/**
 * @author: cangHX
 * @data: 2025/4/22 17:40
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
    fun setJavaCrashMonitorConfig(config: MonitorConfig): IBuilder

    /**
     * 设置主线程卡顿监控配置
     * */
    fun setMainThreadLagMonitorConfig(config: MonitorConfig): IBuilder

    /**
     * 设置 UI 渲染卡顿监控配置
     * */
    fun setUiLagMonitorConfig(config: MonitorConfig): IBuilder

    /**
     * 创建一个配置
     * */
    fun build(): ApmConfig
}
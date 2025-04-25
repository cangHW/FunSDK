package com.proxy.service.apm.info.config

import com.proxy.service.apm.info.config.controller.Controller

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
     * 设置 java crash 控制器
     * */
    fun setJavaCrashMonitorController(controller: Controller): IBuilder

    /**
     * 设置主线程卡顿控制器
     * */
    fun setMainThreadLagMonitorEnable(controller: Controller): IBuilder

    /**
     * 设置 UI 渲染卡顿控制器
     * */
    fun setUiLagMonitorEnable(controller: Controller): IBuilder

    /**
     * 创建一个配置
     * */
    fun build(): ApmConfig
}
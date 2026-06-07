package com.proxy.service.webview.monitor.config.controller.base

import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @date: 2026/6/8 14:25
 * @desc:
 */
abstract class Config<T> : IConfig<T>, IConfigGet {

    private var enable = WebMonitorConstants.ENABLE_LOG_COMMON

    protected abstract fun getInstance(): T

    override fun setEnable(enable: Boolean): T {
        this.enable = enable
        return getInstance()
    }

    override fun getEnable(): Boolean {
        return enable
    }
}
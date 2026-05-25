package com.proxy.service.apm.info.config.controller.base

import com.proxy.service.apm.info.constants.Constants

/**
 * @author: cangHX
 * @date: 2026/5/29 15:26
 * @desc:
 */
abstract class Config<T> : IConfig<T>, IConfigGet {

    private var enable: Boolean = Constants.MONITOR_COMMON_FUN_ENABLE

    protected abstract fun getInstance(): T

    override fun setEnable(enable: Boolean): T {
        this.enable = enable
        return getInstance()
    }

    override fun getEnable(): Boolean {
        return enable
    }
}
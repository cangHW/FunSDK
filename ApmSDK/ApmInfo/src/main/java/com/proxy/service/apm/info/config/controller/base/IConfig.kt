package com.proxy.service.apm.info.config.controller.base

import com.proxy.service.apm.info.constants.Constants

/**
 * @author: cangHX
 * @date: 2026/5/29 15:25
 * @desc:
 */
interface IConfig<T> {
    /**
     * 设置功能是否开启, 默认值 [Constants.MONITOR_COMMON_FUN_ENABLE]
     * */
    fun setEnable(enable: Boolean): T
}
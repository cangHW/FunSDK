package com.proxy.service.webview.monitor.config.controller.base

import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @date: 2026/5/29 15:25
 * @desc:
 */
interface IConfig<T> {
    /**
     * 设置功能是否开启, 默认值 [WebMonitorConstants.ENABLE_LOG_COMMON]
     * */
    fun setEnable(enable: Boolean): T
}
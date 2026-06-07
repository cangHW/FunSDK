package com.proxy.service.webview.monitor.config.controller.request

import com.proxy.service.webview.monitor.config.controller.base.IConfigGet

/**
 * @author: cangHX
 * @date: 2026/6/8 14:12
 * @desc:
 */
interface IRequestConfigGet : IConfigGet {

    /**
     * 预警最小数量
     * */
    fun getRepetitionMinCount(): Int

    /**
     * 预警最大时间
     * */
    fun getRepetitionMaxTime(): Long

    /**
     * 是否展示预警吐司
     * */
    fun getRepetitionToastEnable(): Boolean

    /**
     * 预警最小数量
     * */
    fun getStormMinCount(): Int

    /**
     * 预警最大时间
     * */
    fun getStormMaxTime(): Long

    /**
     * 是否展示预警吐司
     * */
    fun getStormToastEnable(): Boolean

}
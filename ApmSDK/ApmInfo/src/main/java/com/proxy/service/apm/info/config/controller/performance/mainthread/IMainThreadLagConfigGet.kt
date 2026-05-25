package com.proxy.service.apm.info.config.controller.performance.mainthread

import com.proxy.service.apm.info.config.controller.base.IBaseConfigGet

/**
 * [MainThreadLagConfig] 只读访问接口。
 *
 * @author: cangHX
 * @date: 2026/5/22 14:42
 */
interface IMainThreadLagConfigGet : IBaseConfigGet {

    fun getBlockThresholdTime(): Long

}
package com.proxy.service.apm.info.config.controller.performance.uilag

import com.proxy.service.apm.info.config.controller.base.IConfigGet

/**
 * [UiLagConfig] 只读访问接口。
 */
interface IUiLagConfigGet : IConfigGet {

    fun getEnableDebugToast(): Boolean
}

package com.proxy.service.apm.info.config.controller.performance.uilag

import com.proxy.service.apm.info.config.controller.base.IConfig

/**
 * UI 渲染卡顿监控配置构建接口。
 *
 * 判级阈值见 [com.proxy.service.apm.info.constants.Constants] 内 MONITOR_UI_LAG_*；
 * 通知：MODERATE/SEVERE 打日志，仅 SEVERE 在 Debug 下可 Toast。
 */
interface IUiLagConfig : IConfig<IUiLagConfig> {

    /**
     * 是否在 Debug 下对 SEVERE 弹 Toast，默认 `true`。
     */
    fun setEnableDebugToast(enable: Boolean): IUiLagConfig

    fun build(): UiLagConfig
}

package com.proxy.service.apm.info.config.controller.performance.uilag

import com.proxy.service.apm.info.config.controller.base.Config

/**
 * UI 渲染卡顿监控运行时配置。
 */
class UiLagConfig private constructor(
    private val config: IUiLagConfigGet,
) : IUiLagConfigGet by config {

    companion object {
        fun builder(): IUiLagConfig {
            return Builder()
        }
    }

    class Builder : Config<IUiLagConfig>(), IUiLagConfig, IUiLagConfigGet {

        private var enableDebugToast = true

        override fun getInstance(): IUiLagConfig {
            return this
        }

        override fun setEnableDebugToast(enable: Boolean): IUiLagConfig {
            enableDebugToast = enable
            return this
        }

        override fun build(): UiLagConfig {
            return UiLagConfig(this)
        }

        override fun getEnableDebugToast(): Boolean = enableDebugToast

    }
}

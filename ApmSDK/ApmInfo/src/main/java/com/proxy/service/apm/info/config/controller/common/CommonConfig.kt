package com.proxy.service.apm.info.config.controller.common

import com.proxy.service.apm.info.config.controller.base.BaseConfig
import com.proxy.service.apm.info.config.controller.base.IBaseConfigGet

/**
 * @author: cangHX
 * @date: 2025/4/22 18:33
 * @desc:
 */
class CommonConfig private constructor(
    private val controller: IBaseConfigGet
) : IBaseConfigGet by controller {

    companion object {
        fun builder(): ICommonConfig {
            return Builder()
        }
    }

    class Builder : BaseConfig<ICommonConfig>(), ICommonConfig {

        override fun build(): CommonConfig {
            return CommonConfig(this)
        }

        override fun getInstance(): ICommonConfig {
            return this
        }

    }
}
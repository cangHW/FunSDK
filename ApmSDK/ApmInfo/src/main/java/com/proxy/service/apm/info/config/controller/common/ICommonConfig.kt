package com.proxy.service.apm.info.config.controller.common

import com.proxy.service.apm.info.config.controller.base.IBaseConfig

/**
 * @author: cangHX
 * @date: 2026/5/22 14:30
 * @desc:
 */
interface ICommonConfig: IBaseConfig<ICommonConfig> {

    /**
     * 构建
     * */
    fun build(): CommonConfig

}
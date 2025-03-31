package com.proxy.service.apihttp.base.common.config.base

import com.proxy.service.apihttp.base.common.config.common.ICommonBuilder
import com.proxy.service.apihttp.base.common.config.safety.ISafetyBuilder

/**
 * @author: cangHX
 * @data: 2025/3/27 20:55
 * @desc:
 */
interface IBaseConfig<T> : ICommonBuilder<T>, ISafetyBuilder<T> {
}
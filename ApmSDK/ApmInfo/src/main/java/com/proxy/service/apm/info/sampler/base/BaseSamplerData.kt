package com.proxy.service.apm.info.sampler.base

import com.proxy.service.apm.info.sampler.SamplerData

/**
 * @author: cangHX
 * @date: 2026/5/25 10:24
 * @desc:
 */
abstract class BaseSamplerData(val timestampMs: Long) : SamplerData {
}
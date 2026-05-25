package com.proxy.service.apm.info.sampler.impl.device

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.apm.info.sampler.base.BaseSampler

/**
 * @author: cangHX
 * @date: 2026/5/26 17:14
 * @desc:
 */
class DeviceSampler private constructor() : BaseSampler<DeviceSamplerData>(Long.MAX_VALUE) {

    companion object {

        private const val TAG = "${Constants.TAG}DeviceSampler"

        fun create(): DeviceSampler {
            return DeviceSampler()
        }
    }

    override fun getTag(): String {
        return TAG
    }

    override fun start() {
//        super.start()
    }

    override fun stop() {
//        super.stop()
    }

    override fun snapshotsInWindow(startMs: Long, endMs: Long): List<SamplerData> {
        return sampleNow()
    }

    override fun capture() {
        addData(DeviceSamplerData(System.currentTimeMillis()))
    }
}
package com.proxy.service.apm.info.sampler.impl.app

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.apm.info.sampler.base.BaseSampler

/**
 * @author: cangHX
 * @date: 2026/5/26 17:14
 * @desc:
 */
class AppSampler private constructor() : BaseSampler<AppSamplerData>(Long.MAX_VALUE) {

    companion object {

        private const val TAG = "${Constants.TAG}AppSampler"

        fun create(): AppSampler {
            return AppSampler()
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
        addData(AppSamplerData(System.currentTimeMillis()))
    }
}
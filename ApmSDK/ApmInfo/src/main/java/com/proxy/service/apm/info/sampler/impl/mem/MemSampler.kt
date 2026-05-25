package com.proxy.service.apm.info.sampler.impl.mem

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.apm.info.sampler.base.BaseSampler

/**
 * 内存同步采样器。仅用于崩溃等场景调用 [sampleNow]，不要 [start] 周期任务。
 */
class MemSampler private constructor() : BaseSampler<MemSamplerData>(Long.MAX_VALUE, 1) {

    companion object {
        private const val TAG = "${Constants.TAG}MemSampler"

        fun create(): MemSampler = MemSampler()
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

    override fun getTag(): String = TAG

    override fun capture() {
        val data = MemSamplerData(System.currentTimeMillis())
        val result = MemInfoDumper.dumpSync()
        data.appSectionText = result.appSectionText
        data.systemSectionText = result.systemSectionText
        data.errorHint = result.errorHint
        addData(data)
    }
}

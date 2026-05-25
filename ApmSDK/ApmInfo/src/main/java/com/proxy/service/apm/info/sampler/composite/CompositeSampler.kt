package com.proxy.service.apm.info.sampler.composite

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.ICompositeSampler
import com.proxy.service.apm.info.sampler.ICompositeSamplerData
import com.proxy.service.apm.info.sampler.ISampler
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/22 18:33
 * @desc: 组合多个 Sampler（如栈信息采集器 + Cpu信息采集器等）。
 */
class CompositeSampler(
    private vararg val samplers: ISampler<SamplerData>
) : ICompositeSampler {

    companion object {
        private const val TAG = "${Constants.TAG}CompositeSampler"
    }

    override fun start() {
        samplers.forEach {
            try {
                it.start()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }
    }

    override fun stop() {
        samplers.forEach {
            try {
                it.stop()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }
    }

    override fun snapshotsInWindow(startMs: Long, endMs: Long): List<ICompositeSamplerData> {
        val list = ArrayList<CompositeSamplerData>()
        samplers.forEach {
            try {
                val snapshots = it.snapshotsInWindow(startMs, endMs)
                if (snapshots.isNotEmpty()) {
                    val data = CompositeSamplerData(snapshots)
                    list.add(data)
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }
        return list
    }

    override fun sampleNow(): List<ICompositeSamplerData> {
        val list = ArrayList<CompositeSamplerData>()
        samplers.forEach {
            try {
                val snapshots = it.sampleNow()
                if (snapshots.isNotEmpty()) {
                    val data = CompositeSamplerData(snapshots)
                    list.add(data)
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }
        return list
    }
}
package com.proxy.service.apm.info.sampler.impl.stack

import android.os.Looper
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.base.BaseSampler

/**
 * @author: cangHX
 * @date: 2026/5/22 16:05
 * @desc:
 */
class MainThreadStackSampler private constructor(
    intervalMs: Long
) : BaseSampler<MainThreadStackSamplerData>(
    intervalMs
) {

    companion object {

        private const val TAG = "${Constants.TAG}StackSampler"

        fun create(
            intervalMs: Long
        ): MainThreadStackSampler {
            return MainThreadStackSampler(intervalMs)
        }
    }

    val thread = Looper.getMainLooper().thread

    override fun getTag(): String {
        return TAG
    }

    override fun capture() {
        val timestampMs = System.currentTimeMillis()
        val stackTrace = thread.stackTrace.joinToString("\n") { element ->
            "\tat $element"
        }
        val data = MainThreadStackSamplerData(timestampMs)
        data.stackTrace = stackTrace
        addData(data)
    }

}
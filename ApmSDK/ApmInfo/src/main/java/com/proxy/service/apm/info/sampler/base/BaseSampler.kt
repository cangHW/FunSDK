package com.proxy.service.apm.info.sampler.base

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.ISampler
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import java.util.ArrayDeque
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @date: 2026/5/22 16:20
 * @desc:
 */
abstract class BaseSampler<T : BaseSamplerData>(
    private val intervalMs: Long,
    private val slidingWindowSize: Int = Constants.MONITOR_SAMPLER_SLIDING_WINDOW_SIZE
) : ISampler<SamplerData> {

    private val isRunning = AtomicBoolean(false)
    private val buffer = ArrayDeque<T>(slidingWindowSize)

    @Volatile
    private var handlerOption: IHandlerOption? = null

    private fun getHandle(): IHandlerOption? {
        if (handlerOption != null) {
            return handlerOption
        }
        synchronized(this) {
            if (handlerOption != null) {
                return handlerOption
            }
            val groupName = "${this.javaClass.simpleName}_${System.currentTimeMillis()}"
            handlerOption = CsTask.launchTaskGroup(groupName)
            return handlerOption
        }
    }

    private val sampleRunnable = object : Runnable {
        override fun run() {
            try {
                capture()
            } catch (throwable: Throwable) {
                CsLogger.tag(getTag()).w(throwable)
            }

            if (isRunning.get()) {
                getHandle()
                    ?.setDelay(intervalMs, TimeUnit.MILLISECONDS)
                    ?.start(getTag(), this)
            }
        }
    }

    override fun start() {
        if (isRunning.compareAndSet(false, true)) {
            synchronized(buffer) {
                buffer.clear()
            }

            getHandle()?.start(getTag(), sampleRunnable)
        }
    }

    override fun stop() {
        if (isRunning.compareAndSet(true, false)) {
            getHandle()?.clearAllTaskWithTag(getTag())
        }
    }

    override fun snapshotsInWindow(startMs: Long, endMs: Long): List<SamplerData> {
        synchronized(buffer) {
            return buffer.filter { it.timestampMs in startMs..endMs }
        }
    }

    override fun sampleNow(): List<SamplerData> {
        synchronized(buffer) {
            buffer.clear()
        }
        try {
            capture()
        } catch (throwable: Throwable) {
            CsLogger.tag(getTag()).w(throwable)
        }
        synchronized(buffer) {
            return buffer.toList()
        }
    }

    /**
     * 添加数据
     * */
    protected fun addData(data: T) {
        synchronized(buffer) {
            if (buffer.size >= slidingWindowSize) {
                buffer.removeFirst()
            }
            buffer.addLast(data)
        }
    }

    /**
     * 日志tag
     * */
    protected abstract fun getTag(): String

    /**
     * 数据采集
     * */
    protected abstract fun capture()
}
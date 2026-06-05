package com.proxy.service.apm.info.monitor.performance.leak.watcher

import com.proxy.service.apm.info.config.controller.performance.leak.IMemoryLeakConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractHook
import com.proxy.service.apm.info.monitor.performance.leak.watcher.check.RetainedObject
import com.proxy.service.apm.info.monitor.performance.leak.watcher.check.RetainedObjectChecker
import com.proxy.service.apm.info.monitor.performance.leak.watcher.reference.KeyedWeakReference
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.lang.ref.ReferenceQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @date: 2026/6/4 16:51
 * @desc:
 */
class ObjectWatcher(
    private val config: IMemoryLeakConfigGet,
    private val reporter: IReporter<RetainedObject>
) : AbstractHook<Any>() {

    companion object {
        private const val TAG = "${Constants.TAG}ObjectWatcher"
    }

    private val referenceQueue = ReferenceQueue<Any>()
    private val watched = LinkedHashMap<String, KeyedWeakReference>()
    private val checker = RetainedObjectChecker()

    private val running = AtomicBoolean(false)
    private var watchThread: Thread? = null

    override fun start(t: Any?): Boolean {
        if (running.compareAndSet(false, true)) {
            watchThread = Thread({ pollLoop() }, "Apm-MemoryLeak-Watcher")
            watchThread?.isDaemon = true
            watchThread?.start()
        }
        return true
    }

    override fun stop(t: Any?) {
        if (running.compareAndSet(true, false)) {
            watchThread?.interrupt()
            watchThread = null
            synchronized(watched) {
                watched.values.forEach { it.clearReference() }
                watched.clear()
            }
        }
    }

    fun watch(watchedObject: Any, description: String) {
        if (!running.get()) {
            return
        }
        val key = "${watchedObject.javaClass.name}@${System.identityHashCode(watchedObject)}"
        val reference = KeyedWeakReference(
            watchedObject,
            referenceQueue,
            key,
            description,
            System.currentTimeMillis()
        )
        synchronized(watched) {
            watched[key]?.clearReference()
            watched[key] = reference
        }
        CsLogger.tag(TAG).d("Watching $key ($description)")
    }

    private fun pollLoop() {
        while (running.get()) {
            try {
                drainReferenceQueue()
                checkDelayedRetained()
                Thread.sleep(config.getReferenceQueuePollIntervalMs())
            } catch (_: InterruptedException) {
                if (!running.get()) {
                    break
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

    private fun drainReferenceQueue() {
        while (true) {
            val ref = referenceQueue.poll() as? KeyedWeakReference ?: break
            ref.clearReference()
            synchronized(watched) {
                watched.remove(ref.key)
            }
            CsLogger.tag(TAG).d("Recycled ${ref.key}")
        }
    }

    private fun checkDelayedRetained() {
        val now = System.currentTimeMillis()
        val delayMs = config.getRetainedCheckDelayMs()
        val candidates = synchronized(watched) {
            watched.values.filter { now - it.watchTimeMs >= delayMs }.toList()
        }
        for (reference in candidates) {
            CsTask.ioThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    if (!running.get()) {
                        return ""
                    }
                    val retained = checker.confirmRetained(reference) ?: return ""
                    synchronized(watched) {
                        watched.remove(reference.key)
                    }
                    reporter.publish(System.currentTimeMillis(), retained)
                    return ""
                }
            })?.start()
        }
    }
}
package com.proxy.service.core.framework.app.message.event.impl

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseLifecycleActiveSend
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable

/**
 * @author: cangHX
 * @data: 2024/11/29 10:39
 * @desc:
 */
class WorkThreadLifecycleSendImpl(
    callback: WorkThreadEventCallback,
    lifecycleOwner: LifecycleOwner
) : BaseLifecycleActiveSend<WorkThreadEventCallback>(callback, lifecycleOwner) {

    private var taskDisposable: ITaskDisposable? = null

    override fun onActive() {
        taskDisposable = CsTask.computationThread()
            ?.call(object : IMultiRunnable<Any> {
                override fun accept(emitter: MultiRunnableEmitter<Any>) {
                    controller?.forEachCache {
                        emitter.onNext(it)
                    }
                    emitter.onComplete()
                }
            })
            ?.doOnNext(object : IConsumer<Any> {
                override fun accept(value: Any) {
                    try {
                        if (controller?.use(value) == true) {
                            callback?.onWorkEvent(value)
                        }
                    } catch (throwable: Throwable) {
                        CsLogger.tag(EventConfig.TAG).e(throwable)
                    }
                }
            })
            ?.start()
    }

    override fun onInActive() {
        taskDisposable?.dispose()
        taskDisposable = null
    }
}
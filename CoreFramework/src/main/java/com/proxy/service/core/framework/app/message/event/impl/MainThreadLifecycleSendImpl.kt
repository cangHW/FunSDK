package com.proxy.service.core.framework.app.message.event.impl

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseLifecycleActiveSend
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/29 10:39
 * @desc:
 */
class MainThreadLifecycleSendImpl(
    callback: MainThreadEventCallback,
    lifecycleOwner: LifecycleOwner
) : BaseLifecycleActiveSend<MainThreadEventCallback>(callback, lifecycleOwner) {

    override fun onActive() {
        handler?.clearAllTask()
        handler?.start {
            controller?.forEachCache { value ->
                CsTask.mainThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        try {
                            if (controller.use(value)) {
                                callback?.onMainEvent(value)
                            }
                        } catch (throwable: Throwable) {
                            CsLogger.tag(EventConfig.TAG).e(throwable)
                        }
                        return ""
                    }
                })?.start()
            }
        }
    }

    override fun onInActive() {
        handler?.clearAllTask()
    }
}
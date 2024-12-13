package com.proxy.service.core.framework.app.message.event.impl

import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseAlwaysActiveSend
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/29 10:39
 * @desc:
 */
class MainThreadAlwaysSendImpl(
    callback: MainThreadEventCallback
) : BaseAlwaysActiveSend<MainThreadEventCallback>(callback) {

    override fun onActive() {
        handler?.clearAllTask()
        handler?.start{
            controller?.forEachCache { value ->
                CsTask.mainThread()
                    ?.call(object : ICallable<String> {
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
                    })
                    ?.start()
            }
        }
    }
}
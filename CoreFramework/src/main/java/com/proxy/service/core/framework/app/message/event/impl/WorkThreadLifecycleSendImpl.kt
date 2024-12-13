package com.proxy.service.core.framework.app.message.event.impl

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseLifecycleActiveSend
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2024/11/29 10:39
 * @desc:
 */
class WorkThreadLifecycleSendImpl(
    callback: WorkThreadEventCallback,
    lifecycleOwner: LifecycleOwner
) : BaseLifecycleActiveSend<WorkThreadEventCallback>(callback, lifecycleOwner) {

    override fun onActive() {
        handler?.clearAllTask()
        handler?.start{
            controller?.forEachCache { value ->
                try {
                    if (controller.use(value)) {
                        callback?.onWorkEvent(value)
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(EventConfig.TAG).e(throwable)
                }
            }
        }
    }

    override fun onInActive() {
        handler?.clearAllTask()
    }
}
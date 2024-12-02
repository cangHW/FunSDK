package com.proxy.service.core.framework.app.message.event.impl

import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseAlwaysActiveSend
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2024/11/29 10:39
 * @desc:
 */
class WorkThreadAlwaysSendImpl(
    callback: WorkThreadEventCallback
) : BaseAlwaysActiveSend<WorkThreadEventCallback>(callback) {

    override fun onActive() {
        controller?.forEachCache { value ->
            handler?.start {
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
}
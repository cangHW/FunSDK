package com.proxy.service.core.framework.app.message.process.request

import com.proxy.service.core.framework.app.message.process.bean.MessageType
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.callback.OnDataChangedCallback
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.woker.AbstractAsyncWorker
import com.proxy.service.core.framework.app.message.process.woker.AbstractSyncWorker
import com.proxy.service.core.framework.app.message.process.woker.AbstractWorker
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/9/15 19:39
 * @desc:
 */
object RequestDispatch {

    /**
     * 等待初始化的最长时间
     * */
    private const val TIME_OUT_WAITING_FOR_INIT = 3 * 1000L

    private val syncWorkers = CsExcellentMap<String, AbstractWorker>()
    private val asyncWorkers = CsExcellentMap<String, AbstractWorker>()

    fun dispatch(fromPkg: String, message: ShareMessage): ShareMessage {
        val worker: AbstractWorker? = when (message.getMessageType()) {
            MessageType.REQUEST_SYNC -> {
                syncWorkers.getOrWait(
                    message.method,
                    TIME_OUT_WAITING_FOR_INIT,
                    TimeUnit.MILLISECONDS
                )
            }

            MessageType.REQUEST_ASYNC -> {
                asyncWorkers.getOrWait(
                    message.method,
                    TIME_OUT_WAITING_FOR_INIT,
                    TimeUnit.MILLISECONDS
                )
            }

            else -> {
                null
            }
        }

        if (worker == null) {
            return ShareMessageFactory.createResponseError(
                message,
                RequestCallback.ERROR_METHOD_NOT_SUPPORT.toString()
            )
        }

        return worker.getResponse(fromPkg, message)
    }

    fun addWorker(worker: AbstractWorker) {
        if (worker is AbstractSyncWorker) {
            syncWorkers.putSync(worker.getMethodName(), worker)
        } else if (worker is AbstractAsyncWorker) {
            asyncWorkers.putSync(worker.getMethodName(), worker)
        }
    }

    fun removeWorker(worker: AbstractWorker) {
        if (worker is AbstractSyncWorker) {
            syncWorkers.removeSync(worker.getMethodName())
        } else if (worker is AbstractAsyncWorker) {
            asyncWorkers.removeSync(worker.getMethodName())
        }
    }

}
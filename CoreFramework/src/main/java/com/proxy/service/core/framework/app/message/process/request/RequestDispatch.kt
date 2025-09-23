package com.proxy.service.core.framework.app.message.process.request

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
        val worker: AbstractWorker? = when (message.messageType) {
            ShareMessageFactory.DEFAULT_TYPE_REQUEST_ASYNC -> {
                getAsyncWorker(message.method)
            }

            ShareMessageFactory.DEFAULT_TYPE_REQUEST_SYNC -> {
                getSyncWorker(message.method)
            }

            else -> {
                null
            }
        }

        if (worker == null) {
            return ShareMessageFactory.createResponseError(
                message,
                RequestCallback.ERROR_METHOD_NOT_SUPPORT
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
        syncWorkers.removeSync(worker.getMethodName())
        asyncWorkers.removeSync(worker.getMethodName())
    }

    private fun getSyncWorker(method: String): AbstractWorker? {
        return getData(method, syncWorkers)
    }

    private fun getAsyncWorker(method: String): AbstractWorker? {
        return getData(method, asyncWorkers)
    }

    private fun getData(
        method: String,
        map: CsExcellentMap<String, AbstractWorker>
    ): AbstractWorker? {
        var worker = map.get(method)
        if (worker != null) {
            return worker
        }
        val launch = CountDownLatch(1)
        val callback = object : OnDataChangedCallback<Map.Entry<String, AbstractWorker>>() {
            override fun onDataAdd(t: Map.Entry<String, AbstractWorker>) {
                super.onDataAdd(t)
                if (t.key == method) {
                    worker = t.value
                    launch.countDown()
                }
            }
        }
        map.addDataChangedCallback(callback)
        worker = map.get(method)
        if (worker != null) {
            launch.countDown()
        }
        try {
            launch.await(TIME_OUT_WAITING_FOR_INIT, TimeUnit.MILLISECONDS)
        } catch (_: Throwable) {
        }
        map.removeDataChangedCallback(callback)
        return worker
    }

}
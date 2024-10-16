package com.proxy.service.threadpool.info.handler.manager

import android.os.Handler
import android.os.HandlerThread
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.info.constants.Constants
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/7/3 17:29
 * @desc:
 */
object HandlerManager {

    private val threads = HashMap<String, HandlerController>()

    fun getThreadHandler(threadName: String): HandlerController {
        var controller = threads[threadName]
        if (controller?.isCanUse() == true) {
            return controller
        }

        if (controller == null) {
            synchronized(threads) {
                controller = threads[threadName]
                if (controller == null || controller?.isCanUse() != true) {
                    controller?.close()
                    val handler = ThreadHandlerInfo(threadName)
                    threads[threadName] = handler
                    controller = handler
                }
            }
        }
        return controller!!
    }

    private class ThreadHandlerInfo constructor(threadName: String) : HandlerController {

        private val handlerThread: HandlerThread = HandlerThread(threadName)
        private val handler: Handler

        private val isCanUse = AtomicBoolean(true)

        init {
            handlerThread.start()
            handler = Handler(handlerThread.looper)
        }

        override fun getHandler(): Handler {
            return handler
        }

        override fun close() {
            isCanUse.set(false)
            try {
                handlerThread.quit()
            } catch (throwable: Throwable) {
                CsLogger.tag(Constants.TAG).e(throwable)
            }
        }

        override fun closeSafely() {
            isCanUse.set(false)
            try {
                handlerThread.quitSafely()
            } catch (throwable: Throwable) {
                CsLogger.tag(Constants.TAG).e(throwable)
            }
        }

        override fun isCanUse(): Boolean {
            if (!isCanUse.get()) {
                return false
            }
            return handlerThread.isAlive
        }

    }

}
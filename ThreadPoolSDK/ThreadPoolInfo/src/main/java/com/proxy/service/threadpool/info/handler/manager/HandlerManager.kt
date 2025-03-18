package com.proxy.service.threadpool.info.handler.manager

import android.os.Handler
import android.os.HandlerThread
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.info.constants.Constants
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap
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

        synchronized(threads) {
            controller = threads[threadName]
            if (controller == null || controller?.isCanUse() != true) {
                controller?.close()
                val handler = ThreadHandlerInfo(threadName)
                threads[threadName] = handler
                controller = handler
            }
        }
        return controller!!
    }

    private class ThreadHandlerInfo(private val threadName: String) : HandlerController {

        companion object {
            private const val TAG = "${Constants.TAG}_Handler"
        }

        private val cacheTaskMap = WeakHashMap<Any, TaskInfo>()
        private val handlerThread: HandlerThread = HandlerThread(threadName)
        private val handler: Handler

        private val isCanUse = AtomicBoolean(true)

        init {
            handlerThread.start()
            handler = Handler(handlerThread.looper)
        }

        override fun getThreadName(): String {
            return threadName
        }

        override fun getThreadId(): Long {
            return handlerThread.id
        }

        override fun getHandler(): Handler {
            return handler
        }

        override fun startTask(key: Any, value: TaskInfo) {
            synchronized(cacheTaskMap){
                cacheTaskMap.put(key, value)
            }
        }

        override fun finishTask(key: Any) {
            synchronized(cacheTaskMap) {
                cacheTaskMap.remove(key)
            }
        }

        override fun cancelTaskByTag(tag: String) {
            synchronized(cacheTaskMap) {
                val iterator = cacheTaskMap.iterator()
                while (iterator.hasNext()) {
                    val entry = iterator.next()
                    val info = entry.value
                    if (info.tag == tag) {
                        handler.removeCallbacks(info.runnable)
                        iterator.remove()
                    }
                }
            }
        }

        override fun cancelAllTask() {
            synchronized(cacheTaskMap) {
                handler.removeCallbacksAndMessages(null)
                cacheTaskMap.clear()
            }
        }

        override fun close() {
            if (isCanUse.compareAndSet(true, false)) {
                CsLogger.tag(TAG).i("The current task group is ready to shut down immediately.")
                try {
                    cancelAllTask()
                    handlerThread.quit()
                } catch (throwable: Throwable) {
                    CsLogger.tag(Constants.TAG).e(throwable)
                }
                return
            }
            CsLogger.tag(TAG)
                .i("The current task group has been closed. You do not need to close it again.")
        }

        override fun closeSafely() {
            if (isCanUse.compareAndSet(true, false)) {
                CsLogger.tag(TAG).i("The current task group is ready to shut down safely")
                try {
                    handlerThread.quitSafely()
                } catch (throwable: Throwable) {
                    CsLogger.tag(Constants.TAG).e(throwable)
                }
                return
            }
            CsLogger.tag(TAG)
                .i("The current task group has been closed. You do not need to close it again.")
        }

        override fun isCanUse(): Boolean {
            if (!isCanUse.get()) {
                return false
            }
            return handlerThread.isAlive
        }
    }

}
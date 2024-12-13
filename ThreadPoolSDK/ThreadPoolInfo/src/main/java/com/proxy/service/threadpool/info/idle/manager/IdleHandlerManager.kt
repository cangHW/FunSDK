package com.proxy.service.threadpool.info.idle.manager

import android.os.Looper
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.info.constants.Constants
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/11/27 20:34
 * @desc:
 */
object IdleHandlerManager {

    private const val TAG = "${Constants.TAG}_Idle"

    /**
     * 最大查找数量
     * */
    private const val MAX_NUM = 3

    private val isInit = AtomicBoolean(false)
    private val queue = LinkedBlockingQueue<Callable<Boolean>>(Int.MAX_VALUE)

    private fun init() {
        if (!isInit.compareAndSet(false, true)) {
            return
        }
        Looper.myQueue().addIdleHandler {
            var num = 0
            while (num < MAX_NUM && queue.size > 0) {
                val call = queue.poll() ?: break
                if (call.call() == true) {
                    break
                }
                num++
            }
            true
        }
    }

    fun addTask(callable: Callable<Boolean>): Boolean {
        init()
        if (queue.offer(callable)) {
            CsLogger.tag(TAG).i("Idle task added successfully.")
            return true
        }
        CsLogger.tag(TAG)
            .e("Description Failed to add a idle task because the number of existing tasks exceeded Int.MAX_VALUE.")
        return false
    }

}
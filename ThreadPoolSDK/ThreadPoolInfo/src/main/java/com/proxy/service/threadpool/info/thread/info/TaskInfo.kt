package com.proxy.service.threadpool.info.thread.info

import android.os.Looper
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.threadpool.info.constants.Constants
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author: cangHX
 * @data: 2024/6/13 20:17
 * @desc:
 */
class TaskInfo<T> {

    private var hasMainTask = false
    private var firstScheduler: Scheduler? = null
    private var lastScheduler: Scheduler? = null

    var observable: Observable<T>? = null

    fun setSchedulerFirst(scheduler: Scheduler?) {
        if (scheduler == AndroidSchedulers.mainThread()) {
            hasMainTask = true
        }
        this.firstScheduler = scheduler
        this.lastScheduler = scheduler
    }

    fun getScheduler(): Scheduler? {
        return lastScheduler
    }

    fun setScheduler(scheduler: Scheduler?) {
        if (scheduler == AndroidSchedulers.mainThread()) {
            hasMainTask = true
        }
        this.lastScheduler = scheduler
    }

    fun checkBlockIsReady() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            CsLogger.tag(Constants.TAG).d("It is not recommended to use the blocking task in the main thread, which may cause a stalling.")
        }
        if (hasMainTask && Looper.myLooper() == Looper.getMainLooper()) {
            throw IllegalArgumentException("The current task has a main thread node, so the blocking task cannot be executed on the main thread.")
        }
        if (firstScheduler == null && lastScheduler == null) {
            throw IllegalArgumentException("The blocking task cannot be in the same thread as the current task.")
        }
    }

    fun copy(taskInfo: TaskInfo<*>) {
        this.hasMainTask = taskInfo.hasMainTask
        this.firstScheduler = taskInfo.firstScheduler
        this.lastScheduler = taskInfo.lastScheduler
    }

}
package com.proxy.service.threadpool.info.thread

import android.os.Looper
import com.proxy.service.threadpool.base.thread.ThreadService
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import com.proxy.service.threadpool.info.thread.task.TaskOptionImpl
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/7/3 18:03
 * @desc:
 */
class ThreadServiceImpl : ThreadService {

    private val scheduler = ThreadLocal<Scheduler?>()

    override fun <T : Any> call(task: ICallable<T>): ITaskOption<T> {
        val taskInfo = TaskInfo<T>()
        scheduler.get()?.let {
            taskInfo.setSchedulerFirst(it)
        }
        taskInfo.observable = Observable.create {
            try {
                val t = task.accept()
                it.onNext(t)
            } catch (throwable: Throwable) {
                it.onError(throwable)
            }
            it.onComplete()
        }
        if (taskInfo.getScheduler() != AndroidSchedulers.mainThread() || Looper.myLooper() != Looper.getMainLooper()) {
            taskInfo.getScheduler()?.let {
                taskInfo.observable = taskInfo.observable?.subscribeOn(it)
            }
        }
        return TaskOptionImpl(taskInfo)
    }

    override fun <T : Any> call(task: IMultiRunnable<T>): ITaskOption<T> {
        val taskInfo = TaskInfo<T>()
        scheduler.get()?.let {
            taskInfo.setSchedulerFirst(it)
        }
        taskInfo.observable = Observable.create { emitter ->
            try {
                task.accept(object : MultiRunnableEmitter<T> {
                    override fun onNext(value: T) {
                        emitter.onNext(value)
                    }

                    override fun onError(error: Throwable) {
                        emitter.onError(error)
                    }

                    override fun onComplete() {
                        emitter.onComplete()
                    }
                })
            } catch (throwable: Throwable) {
                emitter.onError(throwable)
                emitter.onComplete()
            }
        }
        if (taskInfo.getScheduler() != AndroidSchedulers.mainThread() || Looper.myLooper() != Looper.getMainLooper()) {
            taskInfo.getScheduler()?.let {
                taskInfo.observable = taskInfo.observable?.subscribeOn(it)
            }
        }
        return TaskOptionImpl(taskInfo)
    }

    override fun interval(initialDelay: Long, period: Long, unit: TimeUnit): ITaskOption<Long> {
        val taskInfo = TaskInfo<Long>()
        taskInfo.observable =
            Observable.interval(initialDelay, period, unit, Schedulers.computation())
        taskInfo.getScheduler()?.let {
            taskInfo.observable = taskInfo.observable?.subscribeOn(it)
        } ?: let {
            taskInfo.setScheduler(Schedulers.computation())
        }
        return TaskOptionImpl(taskInfo)
    }

    override fun delay(delay: Long, unit: TimeUnit): ITaskOption<Long> {
        val taskInfo = TaskInfo<Long>()
        scheduler.get()?.let {
            taskInfo.setSchedulerFirst(it)
        }
        taskInfo.observable = Observable.timer(delay, unit, Schedulers.computation())
        taskInfo.getScheduler()?.let {
            taskInfo.observable = taskInfo.observable?.observeOn(it)
        } ?: let {
            taskInfo.setScheduler(Schedulers.computation())
        }
        return TaskOptionImpl(taskInfo)
    }

    override fun ioThread(): ThreadService {
        scheduler.set(Schedulers.io())
        return this
    }

    override fun computationThread(): ThreadService {
        scheduler.set(Schedulers.computation())
        return this
    }

    override fun mainThread(): ThreadService {
        scheduler.set(AndroidSchedulers.mainThread())
        return this
    }

}
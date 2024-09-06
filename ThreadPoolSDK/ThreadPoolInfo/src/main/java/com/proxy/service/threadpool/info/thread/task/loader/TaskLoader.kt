package com.proxy.service.threadpool.info.thread.task.loader

import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.callback.OnRunCallback
import com.proxy.service.threadpool.base.thread.callback.OnStartCallback
import com.proxy.service.threadpool.base.thread.callback.OnSuccessCallback
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.loader.ILoader
import com.proxy.service.threadpool.info.thread.info.TaskDisposableImpl
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author: cangHX
 * @data: 2024/6/13 11:47
 * @desc:
 */
open class TaskLoader<T : Any>(private val taskInfo: TaskInfo<T>) : ILoader<T> {

    private var onStartCallback: OnStartCallback? = null

    override fun setOnStartCallback(callback: OnStartCallback): ILoader<T> {
        this.onStartCallback = callback
        return this
    }

    override fun setOnFailedCallback(callback: OnFailedCallback): ILoader<T> {
        taskInfo.observable = taskInfo.observable?.doOnError {
            callback.onCallback(it)
        }
        return this
    }

    override fun setOnSuccessCallback(callback: OnSuccessCallback): ILoader<T> {
        taskInfo.observable = taskInfo.observable?.doOnComplete {
            callback.onCallback()
        }
        return this
    }

    override fun setOnRunCallback(callback: OnRunCallback<T>): ILoader<T> {
        taskInfo.observable = taskInfo.observable?.doOnNext {
            callback.onCallback(it)
        }
        return this
    }

    override fun start(): ITaskDisposable {
        val taskDisposableImpl = TaskDisposableImpl()
        taskInfo.observable?.subscribe(object : Observer<T> {
            override fun onSubscribe(d: Disposable) {
                taskDisposableImpl.setDisposable(d)
                onStartCallback?.onCallback()
            }

            override fun onError(e: Throwable) {
                CsLogger.d(e, "task has error.")
            }

            override fun onComplete() {
            }

            override fun onNext(t: T) {
            }
        })
        return taskDisposableImpl
    }

    override fun blockGetFirst(): T? {
        onStartCallback?.onCallback()
        taskInfo.checkBlockIsReady()
        try {
            return taskInfo.observable?.blockingFirst()
        }catch (throwable:Throwable){
            CsLogger.d(throwable)
        }
        return null
    }

    override fun blockGetLast(): T? {
        onStartCallback?.onCallback()
        taskInfo.checkBlockIsReady()
        try {
            return taskInfo.observable?.blockingLast()
        }catch (throwable:Throwable){
            CsLogger.d(throwable)
        }
        return null
    }

}
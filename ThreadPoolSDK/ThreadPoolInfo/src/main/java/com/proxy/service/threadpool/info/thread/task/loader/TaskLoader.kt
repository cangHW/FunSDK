package com.proxy.service.threadpool.info.thread.task.loader

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.callback.OnSuccessCallback
import com.proxy.service.threadpool.base.thread.callback.OnStartCallback
import com.proxy.service.threadpool.base.thread.callback.OnCompleteCallback
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.loader.ILoader
import com.proxy.service.threadpool.base.constants.ThreadConstants
import com.proxy.service.threadpool.info.thread.info.TaskDisposableImpl
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.UnknownHostException

/**
 * @author: cangHX
 * @data: 2024/6/13 11:47
 * @desc:
 */
open class TaskLoader<T : Any>(
    private val taskInfo: TaskInfo<T>
) : ILoader<T> {

    private var onStartCallback: OnStartCallback? = null
    private var onFailedCallback: OnFailedCallback? = null
    private var onSuccessCallback: OnSuccessCallback<T>? = null
    private var onCompleteCallback: OnCompleteCallback? = null

    override fun setOnStartCallback(callback: OnStartCallback): ILoader<T> {
        this.onStartCallback = callback
        return this
    }

    override fun setOnFailedCallback(callback: OnFailedCallback): ILoader<T> {
        this.onFailedCallback = callback
        return this
    }

    override fun setOnCompleteCallback(callback: OnCompleteCallback): ILoader<T> {
        this.onCompleteCallback = callback
        return this
    }

    override fun setOnSuccessCallback(callback: OnSuccessCallback<T>): ILoader<T> {
        this.onSuccessCallback = callback
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
                CsLogger.tag(ThreadConstants.TAG).d(e, "task has error.")
                onFailedCallback?.onCallback(e)

                onComplete()
            }

            override fun onComplete() {
                onCompleteCallback?.onCallback()
            }

            override fun onNext(t: T) {
                onSuccessCallback?.onCallback(t)
            }
        })
        return taskDisposableImpl
    }

    override fun blockGetFirst(): T? {
        onStartCallback?.onCallback()
        taskInfo.checkBlockIsReady()
        try {
            val result = taskInfo.observable?.blockingFirst()
            if (result != null) {
                onSuccessCallback?.onCallback(result)
            } else {
                onFailedCallback?.onCallback(UnknownHostException("result is null."))
            }
            return result
        } catch (throwable: Throwable) {
            CsLogger.tag(ThreadConstants.TAG).d(throwable)
            onFailedCallback?.onCallback(throwable)
        } finally {
            onCompleteCallback?.onCallback()
        }
        return null
    }

    override fun blockGetLast(): T? {
        onStartCallback?.onCallback()
        taskInfo.checkBlockIsReady()
        try {
            val result = taskInfo.observable?.blockingLast()
            if (result != null) {
                onSuccessCallback?.onCallback(result)
            } else {
                onFailedCallback?.onCallback(UnknownHostException("result is null."))
            }
            return result
        } catch (throwable: Throwable) {
            CsLogger.tag(ThreadConstants.TAG).d(throwable)
            onFailedCallback?.onCallback(throwable)
        } finally {
            onCompleteCallback?.onCallback()
        }
        return null
    }

}
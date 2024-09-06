package com.proxy.service.threadpool.info.thread.info

import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import io.reactivex.disposables.Disposable

/**
 * @author: cangHX
 * @data: 2024/6/13 18:44
 * @desc:
 */
class TaskDisposableImpl : ITaskDisposable {

    private var isDisposed = false

    private var disposable: Disposable? = null

    @Synchronized
    fun setDisposable(disposable: Disposable) {
        this.disposable = disposable
        if (isDisposed) {
            disposable.dispose()
        }
    }

    @Synchronized
    override fun dispose() {
        isDisposed = true
        disposable?.dispose()
    }

    @Synchronized
    override fun isDisposed(): Boolean {
        if (disposable != null) {
            return disposable?.isDisposed == true
        }
        return isDisposed
    }
}
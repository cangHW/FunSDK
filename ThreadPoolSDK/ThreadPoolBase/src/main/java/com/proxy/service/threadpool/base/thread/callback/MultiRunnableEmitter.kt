package com.proxy.service.threadpool.base.thread.callback

/**
 * @author: cangHX
 * @data: 2024/6/14 17:51
 * @desc:
 */
interface MultiRunnableEmitter<T> {

    /**
     * 发送数据
     * */
    fun onNext(value: T)

    /**
     * 发送错误
     * */
    fun onError(error: Throwable)

    /**
     * 发送完成
     * */
    fun onComplete()
}
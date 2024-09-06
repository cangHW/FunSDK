package com.proxy.service.threadpool.info.thread.task

import io.reactivex.Observable

/**
 * @author: cangHX
 * @data: 2024/6/14 17:02
 * @desc:
 */
interface IRealObserver<T> {

    /**
     * 获取真实任务
     * */
    fun getObserver(): Observable<T>?

}
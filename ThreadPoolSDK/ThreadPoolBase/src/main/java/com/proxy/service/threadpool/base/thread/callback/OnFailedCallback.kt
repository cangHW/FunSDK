package com.proxy.service.threadpool.base.thread.callback

/**
 * @author: cangHX
 * @date: 2024/6/13 19:07
 * @desc:
 */
interface OnFailedCallback {

    fun onCallback(throwable: Throwable)

}
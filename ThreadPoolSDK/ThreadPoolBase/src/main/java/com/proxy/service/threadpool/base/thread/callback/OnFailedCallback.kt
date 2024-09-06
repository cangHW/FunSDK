package com.proxy.service.threadpool.base.thread.callback

/**
 * @author: cangHX
 * @data: 2024/6/13 19:07
 * @desc:
 */
interface OnFailedCallback {

    fun onCallback(throwable: Throwable)

}
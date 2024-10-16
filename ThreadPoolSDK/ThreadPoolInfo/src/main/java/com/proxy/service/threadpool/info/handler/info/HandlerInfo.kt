package com.proxy.service.threadpool.info.handler.info

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.info.constants.Constants
import com.proxy.service.threadpool.info.handler.manager.HandlerController

/**
 * @author: cangHX
 * @data: 2024/7/3 18:10
 * @desc:
 */
class HandlerInfo {

    var handlerController: HandlerController? = null

    var delay: Long = 0
    var uptimeMillis: Long = 0

    fun doRun(runnable: Runnable) {
        try {
            if (delay > 0) {
                handlerController?.getHandler()?.postDelayed(runnable, delay)
            } else if (uptimeMillis > 0) {
                handlerController?.getHandler()?.postAtTime(runnable, uptimeMillis)
            } else {
                handlerController?.getHandler()?.post(runnable)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(Constants.TAG).e(throwable)
        }
    }

}
package com.proxy.service.threadpool.info.handler.info

import com.proxy.service.threadpool.info.handler.manager.HandlerController

/**
 * @author: cangHX
 * @data: 2024/7/3 18:10
 * @desc:
 */
class HandlerInfo(val handlerController: HandlerController) {

    var delay: Long = 0
    var uptimeMillis: Long = 0

}
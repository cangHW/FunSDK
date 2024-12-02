package com.proxy.service.core.framework.app.message.event.controller

import com.proxy.service.core.framework.app.message.event.base.IController

/**
 * @author: cangHX
 * @data: 2024/11/29 15:29
 * @desc:
 */
class SingleDataController : IController {

    private var any: Any? = null

    override fun addCache(any: Any) {
        this.any = any
    }

    override fun use(any: Any): Boolean {
        return this.any == any
    }

    override fun forEachCache(callback: (any: Any) -> Unit) {
        any?.let {
            callback(it)
        }
    }
}
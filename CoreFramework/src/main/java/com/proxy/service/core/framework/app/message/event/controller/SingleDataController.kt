package com.proxy.service.core.framework.app.message.event.controller

import com.proxy.service.core.framework.app.message.event.base.IController

/**
 * @author: cangHX
 * @data: 2024/11/29 15:29
 * @desc:
 */
class SingleDataController : IController {

    private val lock = Any()

    private var any: Any? = null

    override fun addCache(any: Any) {
        synchronized(lock) {
            this.any = any
        }
    }

    override fun use(any: Any): Boolean {
        synchronized(lock) {
            val flag = this.any == any
            if (flag) {
                this.any = null
            }
            return flag
        }
    }

    override fun forEachCache(callback: (any: Any) -> Unit) {
        any?.let {
            callback(it)
        }
    }
}
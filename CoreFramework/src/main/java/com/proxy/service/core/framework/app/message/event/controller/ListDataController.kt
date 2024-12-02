package com.proxy.service.core.framework.app.message.event.controller

import com.proxy.service.core.framework.app.message.event.base.IController

/**
 * @author: cangHX
 * @data: 2024/11/29 15:29
 * @desc:
 */
class ListDataController : IController {

    private val list = ArrayList<Any>()

    override fun addCache(any: Any) {
        list.add(any)
    }

    override fun use(any: Any): Boolean {
        return list.remove(any)
    }

    override fun forEachCache(callback: (any: Any) -> Unit) {
        val iterator = list.iterator()
        while (iterator.hasNext()){
            callback(iterator.next())
        }
    }
}
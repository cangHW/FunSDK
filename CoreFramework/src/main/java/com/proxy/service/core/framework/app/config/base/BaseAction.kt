package com.proxy.service.core.framework.app.config.base

/**
 * @author: cangHX
 * @data: 2024/12/26 10:04
 * @desc:
 */
abstract class BaseAction {
    private val threadLocal = ThreadLocal<Boolean>()

    protected fun setSave(isSave: Boolean) {
        threadLocal.set(isSave)
    }

    protected fun isSave(): Boolean {
        val isSave = threadLocal.get() ?: true
        threadLocal.remove()
        return isSave
    }
}
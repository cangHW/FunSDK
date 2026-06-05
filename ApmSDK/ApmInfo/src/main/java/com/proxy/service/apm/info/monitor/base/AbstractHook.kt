package com.proxy.service.apm.info.monitor.base

/**
 * @author: cangHX
 * @date: 2026/5/22 17:32
 * @desc:
 */
abstract class AbstractHook<T : Any?> {

    abstract fun start(t: T? = null): Boolean

    abstract fun stop(t: T? = null)

}
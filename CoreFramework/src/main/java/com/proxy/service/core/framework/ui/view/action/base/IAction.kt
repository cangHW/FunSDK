package com.proxy.service.core.framework.ui.view.action.base

/**
 * @author: cangHX
 * @data: 2026/1/8 16:57
 * @desc:
 */
abstract class IAction<R> {

    /**
     * 安全模式, 是否自动拦截异常
     * */
    protected var isSafeMode = true

    /**
     * 安全模式, 如果为 true, 则自动拦截相关异常, 默认为 true
     * */
    @Suppress("UNCHECKED_CAST")
    fun safe(enable: Boolean): R {
        this.isSafeMode = enable
        return this as R
    }

}
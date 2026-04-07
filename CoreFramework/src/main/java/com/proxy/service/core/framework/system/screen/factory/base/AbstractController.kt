package com.proxy.service.core.framework.system.screen.factory.base

import com.proxy.service.core.constants.CoreConfig
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/3/31 18:44
 * @desc:
 */
abstract class AbstractController {

    protected val tag = "${CoreConfig.TAG}ScreenRotation"

    private val isInit = AtomicBoolean(false)
    private val isStart = AtomicBoolean(false)

    /**
     * 初始化
     * */
    fun init() {
        if (isInit.compareAndSet(false, true)) {
            onInit()
        }
    }

    protected abstract fun onInit()

    /**
     * 是否可用
     * */
    open fun canUse(): Boolean {
        return true
    }

    /**
     * 开始
     * */
    fun start() {
        if (isStart.compareAndSet(false, true)) {
            onStart()
        }
    }

    protected abstract fun onStart()

    /**
     * 暂停
     * */
    fun stop() {
        if (isStart.compareAndSet(true, false)) {
            onStop()
        }
    }

    protected abstract fun onStop()

}
package com.proxy.service.core.framework.app.message.event.impl.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.message.event.base.BaseSend
import com.proxy.service.core.framework.app.message.event.base.IEvent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/12/2 11:38
 * @desc:
 */
abstract class BaseLifecycleActiveSend<T : IEvent>(
    callback: T?,
    private var lifecycleOwner: LifecycleOwner?
) : BaseSend<T>(callback), LifecycleEventObserver {

    private val isActive = AtomicBoolean(false)

    fun initLifecycle() {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    override fun checkState() {
        lifecycleOwner?.lifecycle?.currentState?.let {
            changeActiveState(it)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        var currentState = lifecycleOwner?.lifecycle?.currentState
        if (currentState == Lifecycle.State.DESTROYED) {
            lifecycleOwner?.lifecycle?.removeObserver(this)
            lifecycleOwner = null
            callback = null
            return
        }
        var prevState: Lifecycle.State? = null
        while (prevState != currentState) {
            prevState = currentState
            prevState?.let {
                changeActiveState(it)
            }
            currentState = lifecycleOwner?.lifecycle?.currentState
        }
    }

    private fun changeActiveState(prevState: Lifecycle.State) {
        if (prevState.isAtLeast(Lifecycle.State.STARTED)) {
            if (isActive.compareAndSet(false, true)) {
                onActive()
            }
        } else {
            if (isActive.compareAndSet(true, false)) {
                onInActive()
            }
        }
    }

    /**
     * 活跃状态, 可以发送数据
     * */
    abstract fun onActive()

    /**
     * 非活跃状态, 禁止发送数据
     * */
    abstract fun onInActive()

}
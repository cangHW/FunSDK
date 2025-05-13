package com.proxy.service.core.framework.app.message.event.impl.base

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.message.event.base.BaseSend
import com.proxy.service.core.framework.app.message.event.base.IEvent
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2024/12/2 11:38
 * @desc:
 */
abstract class BaseAlwaysActiveSend<T : IEvent>(callback: T?):BaseSend<T>(callback) {

    protected val handler = CsTask.launchTaskGroup("${CoreConfig.TAG}EventAlwaysActive")

    override fun checkState() {
        onActive()
    }

    /**
     * 活跃状态, 可以发送数据
     * */
    abstract fun onActive()
}
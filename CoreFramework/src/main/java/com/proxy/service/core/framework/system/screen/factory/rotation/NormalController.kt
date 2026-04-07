package com.proxy.service.core.framework.system.screen.factory.rotation

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.factory.base.AbstractController
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/3/31 18:45
 * @desc:
 */
class NormalController private constructor(
    private val callback: RotationChangedCallback
) : AbstractController(), Runnable {

    companion object {
        private const val DELAY_TIME = 50L

        fun create(callback: RotationChangedCallback): NormalController {
            return NormalController(callback)
        }
    }

    private var handler: IHandlerOption? = null

    override fun onInit() {
        handler = CsTask.launchTaskGroup("Cs-Screen-Rotation-Normal")
    }

    override fun onStart() {
        handler?.start(this)
    }

    override fun onStop() {
        handler?.start{
            handler?.clearAllTask()
        }
    }

    override fun run() {
        try {
            callback.onRotationChanged(-1)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        handler?.setDelay(DELAY_TIME, TimeUnit.MILLISECONDS)?.start(this)
    }

}
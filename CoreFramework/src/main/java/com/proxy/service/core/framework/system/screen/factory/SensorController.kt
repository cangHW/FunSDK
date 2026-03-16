package com.proxy.service.core.framework.system.screen.factory

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.OrientationEventListener
import android.view.WindowManager
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/3/16 15:18
 * @desc:
 */
class SensorController {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Sensor"

        val instance by lazy {
            SensorController()
        }
    }

    private var enable = false
    private val isStart = AtomicBoolean(false)
    private var windowManager: WindowManager? = null

    private var rotation: RotationEnum? = null
    private var orientationListener: OrientationEventListener? = null
    private val callbacks: ISet<ScreenRotationCallback> = CsExcellentSet()


    private fun getRotationByDisplay(activity: Activity? = null): RotationEnum? {
        val degree = if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display?.rotation
        } else {
            windowManager?.defaultDisplay?.rotation
        }

        return RotationEnum.valueOf(degree)
    }

    init {
        val application = CsContextManager.getApplication()
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as? WindowManager?
        orientationListener = object : OrientationEventListener(application) {
            override fun onOrientationChanged(orientation: Int) {
                val ro = getRotationByDisplay()
                if (rotation != ro) {
                    rotation = ro
                    callbacks.forEachAsync { callback ->
                        callRotation(callback)
                    }
                }
            }
        }
        if (orientationListener?.canDetectOrientation() == true) {
            this.enable = true

            this.rotation = getRotationByDisplay(CsContextManager.getTopActivity())
        } else {
            CsLogger.tag(TAG).e("传感器不可用, 无法检测方向变化.")
        }
    }

    fun getRotationEnum(activity: Activity? = null): RotationEnum? {
        return if (enable) {
            rotation
        } else {
            getRotationByDisplay(activity)
        }
    }

    fun addCallback(callback: ScreenRotationCallback) {
        if (enable) {
            callbacks.putSync(callback)

            if (callbacks.size() > 0) {
                if (isStart.compareAndSet(false, true)) {
                    orientationListener?.enable()
                }
            }
        } else {
            DisplayController.instance.addCallback(callback)
        }
    }

    fun removeCallback(callback: ScreenRotationCallback) {
        if (enable) {
            callbacks.removeSync(callback)

            if (callbacks.size() <= 0) {
                if (isStart.compareAndSet(true, false)) {
                    orientationListener?.disable()
                }
            }
        } else {
            DisplayController.instance.removeCallback(callback)
        }
    }

    private fun callRotation(callback: ScreenRotationCallback) {
        val rotation = this.rotation ?: return
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onRotation(rotation)
                return ""
            }
        })?.start()
    }
}
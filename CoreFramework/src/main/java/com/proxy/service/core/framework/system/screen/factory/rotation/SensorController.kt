package com.proxy.service.core.framework.system.screen.factory.rotation

import android.view.OrientationEventListener
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.system.screen.factory.base.AbstractController

/**
 * @author: cangHX
 * @data: 2026/3/31 18:45
 * @desc:
 */
class SensorController private constructor(
    private val callback: RotationChangedCallback
) : AbstractController() {

    companion object {
        fun create(callback: RotationChangedCallback): SensorController {
            return SensorController(callback).apply {
                init()
            }
        }
    }

    private var orientationListener: OrientationEventListener? = null

    override fun onInit() {
        val application = CsContextManager.getApplication()
        orientationListener = object : OrientationEventListener(application) {
            override fun onOrientationChanged(orientation: Int) {
                callback.onRotationChanged(orientation)
            }
        }
    }

    override fun canUse(): Boolean {
        return orientationListener?.canDetectOrientation() == true
    }

    override fun onStart() {
        orientationListener?.enable()
    }

    override fun onStop() {
        orientationListener?.disable()
    }

}
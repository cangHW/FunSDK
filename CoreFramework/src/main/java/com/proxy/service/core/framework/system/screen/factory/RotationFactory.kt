package com.proxy.service.core.framework.system.screen.factory

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.base.IMap
import com.proxy.service.core.framework.collections.type.Type
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.framework.system.screen.factory.base.AbstractController
import com.proxy.service.core.framework.system.screen.factory.rotation.NormalController
import com.proxy.service.core.framework.system.screen.factory.rotation.RotationChangedCallback
import com.proxy.service.core.framework.system.screen.factory.rotation.SensorController
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2026/3/16 15:18
 * @desc:
 */
class RotationFactory : RotationChangedCallback {

    companion object {
        private const val TAG = "${CoreConfig.TAG}ScreenRotation"

        val instance by lazy {
            RotationFactory()
        }
    }

    private class Info {
        var activity: Activity? = null
        var rotation: RotationEnum? = null
    }

    private var controller: AbstractController? = null
    private var windowManager: WindowManager? = null
    private val callbacks: IMap<ScreenRotationCallback, Info> = CsExcellentMap(Type.WEAK)

    private var rotation: RotationEnum? = null


    init {
        val application = CsContextManager.getApplication()
        windowManager = application.getSystemService(Context.WINDOW_SERVICE) as? WindowManager?

        controller = SensorController.create(this)
        if (controller?.canUse() != true) {
            CsLogger.tag(TAG).e("传感器不可用.")
            controller = NormalController.create(this)
        }
        this.rotation = getRotationEnum(CsContextManager.getTopActivity())
    }

    fun getRotationEnum(activity: Activity? = null): RotationEnum? {
        val degree = if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display?.rotation
        } else {
            windowManager?.defaultDisplay?.rotation
        }

        return RotationEnum.valueOf(degree)
    }

    fun addWeakCallback(activity: Activity?, callback: ScreenRotationCallback) {
        CsLogger.tag(TAG).d("addWeakCallback. activity=$activity, callback=$callback")
        val info = Info()
        info.activity = activity
        callbacks.putSync(callback, info)

        if (callbacks.size() > 0) {
            controller?.start()
        }
    }

    fun removeCallback(callback: ScreenRotationCallback) {
        CsLogger.tag(TAG).d("removeCallback. callback=$callback")
        callbacks.removeSync(callback)

        if (callbacks.size() <= 0) {
            rotation = null
            controller?.stop()
        }
    }

    private fun callRotation(callback: ScreenRotationCallback, rotation: RotationEnum) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onRotation(rotation)
                return ""
            }
        })?.start()
    }

    override fun onRotationChanged(orientation: Int) {
        val windowRotation = getRotationEnum() ?: return
        if (this.rotation == windowRotation) {
            return
        }
        this.rotation = windowRotation
        CsLogger.tag(TAG).d("onRotationChanged. windowRotation=${windowRotation.name}")

        callbacks.forEachAsync { callback, info ->
            val activity = info.activity
            if (activity == null) {
                callRotation(callback, windowRotation)
                return@forEachAsync
            }

            val actRotation = getRotationEnum(activity) ?: return@forEachAsync
            CsLogger.tag(TAG).d("actRotation=${actRotation.name}, activity=$activity")
            if (actRotation != info.rotation) {
                info.rotation = actRotation
                callRotation(callback, actRotation)
            }
        }
    }
}
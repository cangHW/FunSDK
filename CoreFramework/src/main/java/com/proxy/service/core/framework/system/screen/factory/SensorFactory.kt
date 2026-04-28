package com.proxy.service.core.framework.system.screen.factory

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.collections.type.Type
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.callback.SensorRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.framework.system.screen.factory.base.AbstractController
import com.proxy.service.core.framework.system.screen.factory.rotation.RotationChangedCallback
import com.proxy.service.core.framework.system.screen.factory.rotation.SensorController
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2026/4/2 11:31
 * @desc:
 */
class SensorFactory : RotationChangedCallback {

    companion object {
        private const val TAG = "${CoreConfig.TAG}ScreenSensor"

        val instance by lazy {
            SensorFactory()
        }
    }

    private var controller: AbstractController? = null
    private val callbacks: ISet<SensorRotationCallback> = CsExcellentSet(Type.WEAK)

    init {
        controller = SensorController.create(this)
    }

    fun addWeakCallback(callback: SensorRotationCallback) {
        CsLogger.tag(TAG).d("addWeakCallback. callback=$callback")
        callbacks.putSync(callback)

        if (callbacks.size() > 0) {
            controller?.start()
        }
    }

    fun removeCallback(callback: SensorRotationCallback) {
        CsLogger.tag(TAG).d("removeCallback. callback=$callback")
        callbacks.removeSync(callback)

        if (callbacks.size() <= 0) {
            controller?.stop()
        }
    }

    override fun onRotationChanged(orientation: Int) {
        when (orientation) {
            in 0..45, in 315..360 -> {
                // 竖屏
//                CsLogger.tag(TAG).d("onRotationChanged. 竖屏")
                callRotation(orientation, RotationEnum.ROTATION_0)
            }

            in 135..225 -> {
                // 倒置竖屏
//                CsLogger.tag(TAG).d("onRotationChanged. 倒置竖屏")
                callRotation(orientation, RotationEnum.ROTATION_180)
            }

            in 45..135 -> {
                // 横屏（逆时针旋转 90°）
//                CsLogger.tag(TAG).d("onRotationChanged. 横屏（逆时针旋转 90°）")
                callRotation(orientation, RotationEnum.ROTATION_270)
            }

            in 225..315 -> {
                // 横屏（顺时针旋转 90°）
//                CsLogger.tag(TAG).d("onRotationChanged. 横屏（顺时针旋转 90°）")
                callRotation(orientation, RotationEnum.ROTATION_90)
            }
        }
    }

    private fun callRotation(orientation: Int, rotation: RotationEnum) {
        callbacks.forEachAsync {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    it.onRotation(orientation, rotation)
                    return ""
                }
            })?.start()
        }
    }

}
package com.proxy.service.camera.info.view.base

import android.graphics.RectF
import android.view.MotionEvent
import com.proxy.service.camera.base.mode.loader.CameraMeteringMode
import com.proxy.service.camera.base.mode.view.CameraViewMeteringMode
import com.proxy.service.camera.base.mode.loader.bean.MeteringAreaInfo
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.camera.info.view.touch.mode.MeteringModeDispatch

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractCameraFunView(
    private val config: CameraViewConfig,
) : AbstractCameraView(config), ICameraView, MeteringModeDispatch.OnCameraMeteringIntercept {

    private var cameraViewMeteringMode = config.cameraViewMeteringMode

    override fun setCameraViewMeteringMode(mode: CameraViewMeteringMode) {
        this.cameraViewMeteringMode = mode
    }

    /**
     * 检测是否触发触摸测光
     * */
    override fun onTouchMeteringIntercept(
        event: MotionEvent,
        viewWidth: Int,
        viewHeight: Int
    ): MeteringModeDispatch.MeteringTouchResult? {
        val mode = cameraViewMeteringMode as? CameraViewMeteringMode.TouchMode? ?: return null

        val halfW = mode.width / 2
        val halfH = mode.height / 2

        val viewRect = RectF(
            event.x - halfW,
            event.y - halfH,
            event.x + halfW,
            event.y + halfH
        )
        createMeteringAreaInfo(viewRect, viewWidth, viewHeight)?.let {
            cameraController?.setCameraMeteringMode(CameraMeteringMode.FixedMode(listOf(it)))
        }

        return MeteringModeDispatch.MeteringTouchResult(
            rect = if (config.cameraViewMeteringRectVisible) viewRect else null,
            needReset = true
        )
    }

    override fun onTouchMeteringReset() {
        cameraController?.setCameraMeteringMode(CameraMeteringMode.AutoMode)
    }

    /**
     * 将 View 坐标系下的测光区域转换为 Camera2 需要的传感器坐标。
     * */
    protected open fun createMeteringAreaInfo(
        viewRect: RectF,
        viewWidth: Int,
        viewHeight: Int
    ): MeteringAreaInfo? {
        return null
    }

}
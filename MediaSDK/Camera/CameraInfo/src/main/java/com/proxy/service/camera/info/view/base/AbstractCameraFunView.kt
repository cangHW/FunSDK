package com.proxy.service.camera.info.view.base

import android.graphics.RectF
import android.view.MotionEvent
import com.proxy.service.camera.base.mode.loader.CameraAfMode
import com.proxy.service.camera.base.mode.view.CameraViewAfMode
import com.proxy.service.camera.base.mode.loader.af.FocusAreaInfo
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.camera.info.view.touch.mode.AfModeDispatch

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractCameraFunView(
    config: CameraViewConfig,
) : AbstractCameraView(config), ICameraView, AfModeDispatch.OnCameraAfIntercept {

    private var cameraViewAfMode = config.cameraViewAfMode

    override fun setCameraViewAfMode(mode: CameraViewAfMode) {
        this.cameraViewAfMode = mode
    }

    /**
     * 检测是否触发手动对焦
     * */
    override fun onTouchAfIntercept(event: MotionEvent): RectF? {
        val mode = cameraViewAfMode as? CameraViewAfMode.AfTouchMode? ?: return null

        val halfW = mode.width
        val halfH = mode.height

        val x = event.x - halfW
        val y = event.y - halfH

        val list = ArrayList<FocusAreaInfo>()
        list.add(
            FocusAreaInfo.create(
                x.toInt(),
                y.toInt(),
                mode.width,
                mode.height,
                FocusAreaInfo.WEIGHT_MAX
            )
        )
        cameraController?.setCameraAfMode(CameraAfMode.AfFixedMode(list))

        return RectF(x, y, event.x + halfW, event.y + halfH)
    }


}
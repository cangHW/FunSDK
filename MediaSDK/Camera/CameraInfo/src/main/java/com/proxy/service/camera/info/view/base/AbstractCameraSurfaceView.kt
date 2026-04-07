package com.proxy.service.camera.info.view.base

import android.util.Size
import android.view.View
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.camera.info.view.config.UserOutSize
import com.proxy.service.camera.info.view.config.UserPreviewSize
import com.proxy.service.camera.info.view.manager.CalculateSizeManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum

/**
 * @author: cangHX
 * @data: 2026/3/30 14:09
 * @desc:
 */
abstract class AbstractCameraSurfaceView(
    config: CameraViewConfig,
    private val view: View
) : AbstractCameraFunView(config), ICameraView {

    private val calculateSizeManager = CalculateSizeManager.create().apply {
        config.userSize.forEach {
            if (it is UserPreviewSize) {
                setUserPreviewSize(it.mode, it.faceMode, it.size)
            } else if (it is UserOutSize) {
                setUserOutSize(it.mode, it.faceMode, it.size)
            }
        }
    }

    protected var surfaceWidth: Int = 0
    protected var surfaceHeight: Int = 0

    private var rotationCallbackAdded: Boolean = false
    protected var rotation: RotationEnum? = null

    private val screenRotationCallback = object : ScreenRotationCallback {
        override fun onRotation(rotation: RotationEnum) {
            CsLogger.tag(tag).d("onRotation. rotation=${rotation.name}")
            if (this@AbstractCameraSurfaceView.rotation == rotation) {
                return
            }
            this@AbstractCameraSurfaceView.rotation = rotation
            val viewW = if (view.width > 0) view.width else surfaceWidth
            val viewH = if (view.height > 0) view.height else surfaceHeight
            if (viewW > 0 && viewH > 0) {
                surfaceWidth = viewW
                surfaceHeight = viewH

                startCameraPreview()
            }
        }
    }


    override fun setPreviewSize(width: Int, height: Int) {
        if (surfaceWidth != width || surfaceHeight != height) {
            calculateSizeManager.setUserPreviewSize(null, null, Size(width, height))
            startCameraPreview()
        }
    }

    /**
     * 开始相机预览, 调整预览尺寸、旋转等
     * */
    protected abstract fun startCameraPreview()


    override fun chooseCaptureMode(): ICameraCaptureController {
        val controller = super.chooseCaptureMode()
        getCalculateOutSize(surfaceWidth, surfaceHeight)?.let {
            controller.setPictureCaptureSize(it.width, it.height)
            startPreview()
        }
        return controller
    }

    override fun chooseRecordMode(): ICameraRecordController {
        val controller = super.chooseRecordMode()
        getCalculateOutSize(surfaceWidth, surfaceHeight)?.let {
            controller.setVideoRecordSize(it.width, it.height)
            startPreview()
        }
        return controller
    }

    override fun releaseCamera() {
        super.releaseCamera()
        finishCallScreenRotation()
    }

    /**
     * 开始屏幕旋转监听
     * */
    protected fun startCallScreenRotation() {
        if (!rotationCallbackAdded) {
            CsScreenUtils.addWeakScreenRotationCallback(screenRotationCallback)
            rotationCallbackAdded = true
        }
    }

    /**
     * 结束屏幕旋转监听
     * */
    protected fun finishCallScreenRotation() {
        if (rotationCallbackAdded) {
            CsScreenUtils.removeScreenRotationCallback(screenRotationCallback)
            rotationCallbackAdded = false
        }
    }

    protected fun getCalculatePreviewSize(width: Int, height: Int): Size? {
        val cameraFaceMode = cameraController?.getOpenedCameraMode()
        val cameraMode = cameraController?.getChooseMode()
        if (cameraFaceMode == null) {
            return null
        }
        val size = calculateSizeManager.getPreviewSize(
            cameraMode,
            cameraFaceMode,
            width,
            height
        )

        size?.let {
            CsLogger.tag(tag).d("CalculatePreviewSize. width=${it.width}, height=${it.height}")
        }

        return size
    }

    private fun getCalculateOutSize(width: Int, height: Int): Size? {
        val cameraFaceMode = cameraController?.getOpenedCameraMode()
        val cameraMode = cameraController?.getChooseMode()
        if (cameraFaceMode == null || cameraMode == null) {
            return null
        }
        val size = calculateSizeManager.getOutSize(
            cameraMode,
            cameraFaceMode,
            width,
            height
        )

        size?.let {
            CsLogger.tag(tag).d("CalculateOutSize. width=${it.width}, height=${it.height}")
        }

        return size
    }

}
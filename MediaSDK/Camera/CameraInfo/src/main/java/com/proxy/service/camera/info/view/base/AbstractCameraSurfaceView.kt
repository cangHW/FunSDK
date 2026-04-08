package com.proxy.service.camera.info.view.base

import android.util.Size
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.service.media.CsMediaCamera

/**
 * @author: cangHX
 * @data: 2026/3/30 14:09
 * @desc:
 */
abstract class AbstractCameraSurfaceView(
    config: CameraViewConfig
) : AbstractCameraFunView(config), ICameraView {

    private var userPreviewSurfaceWidth: Int = -1
    private var userPreviewSurfaceHeight: Int = -1

    private var rotationCallbackAdded: Boolean = false
    protected var rotation: RotationEnum? = null

    private val screenRotationCallback = object : ScreenRotationCallback {
        override fun onRotation(rotation: RotationEnum) {
            CsLogger.tag(tag).d("onRotation. rotation=${rotation.name}")
            if (this@AbstractCameraSurfaceView.rotation == rotation) {
                return
            }
            this@AbstractCameraSurfaceView.rotation = rotation

            startCameraPreview()
        }
    }


    override fun setPreviewSize(width: Int, height: Int) {
        CsLogger.tag(tag).d("setPreviewSize. width=$width, height=$height")
        if (width <= 0 || height <= 0) {
            CsLogger.tag(tag).e("The preview size cannot be less than 0.")
            return
        }
        this.userPreviewSurfaceWidth = width
        this.userPreviewSurfaceHeight = height
    }

    /**
     * 开始相机预览, 调整预览尺寸、旋转等
     * */
    protected abstract fun startCameraPreview()

    override fun startPreview() {
//        super.startPreview()
        startCameraPreview()
    }

    override fun chooseCaptureMode(): ICameraCaptureController {
        val controller = super.chooseCaptureMode()
        startPreview()
        return controller
    }

    override fun chooseRecordMode(): ICameraRecordController {
        val controller = super.chooseRecordMode()
        startPreview()
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

    protected fun getCalculatePreviewSize(
        faceMode: CameraFaceMode,
        width: Int,
        height: Int
    ): Size? {

        if (width <= 0 || height <= 0) {
            return null
        }

        val size = if (userPreviewSurfaceWidth > 0 || userPreviewSurfaceHeight > 0) {
            Size(userPreviewSurfaceWidth, userPreviewSurfaceHeight)
        } else {
            val supportSizes = when (cameraController?.getChooseMode()) {
                CameraFunMode.CAPTURE -> {
                    CsMediaCamera.getSupportedCaptureSizes(faceMode) ?: ArrayList()
                }

                CameraFunMode.RECORD -> {
                    CsMediaCamera.getSupportedRecordSizes(faceMode) ?: ArrayList()
                }

                else -> {
                    CsMediaCamera.getSupportedPreviewSizes(faceMode) ?: ArrayList()
                }
            }
            CameraUtils.calculateSize(supportSizes, width, height)
        }

        size?.let {
            CsLogger.tag(tag).d("CalculatePreviewSize. width=${it.width}, height=${it.height}")
        }

        return size
    }

}
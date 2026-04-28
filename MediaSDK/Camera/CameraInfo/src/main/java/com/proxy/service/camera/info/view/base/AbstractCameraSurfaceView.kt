package com.proxy.service.camera.info.view.base

import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.util.Size
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.base.mode.loader.bean.MeteringAreaInfo
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

        val size = if (userPreviewSurfaceWidth > 0 && userPreviewSurfaceHeight > 0) {
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
            CameraUtils.calculateSize(supportSizes, width, height)?.toSize()
        }

        size?.let {
            CsLogger.tag(tag).d("CalculatePreviewSize. width=${it.width}, height=${it.height}")
        }

        return size
    }

    override fun createMeteringAreaInfo(
        viewRect: RectF,
        viewWidth: Int,
        viewHeight: Int
    ): MeteringAreaInfo? {
        val sensorRect = mapViewRectToSensorRect(viewRect, viewWidth, viewHeight) ?: return null
        return MeteringAreaInfo.create(
            sensorRect.left,
            sensorRect.top,
            sensorRect.width(),
            sensorRect.height(),
            MeteringAreaInfo.WEIGHT_MAX
        )
    }

    /**
     * 将 View 坐标系下的矩形转换为传感器 active array 坐标系下的矩形。
     *
     * 转换过程会先构建传感器到 View 的显示矩阵，再叠加子类提供的预览显示矩阵和镜像信息，
     * 最后通过逆矩阵把触摸区域反算回传感器坐标。
     * */
    protected fun mapViewRectToSensorRect(
        viewRect: RectF,
        viewWidth: Int,
        viewHeight: Int
    ): Rect? {
        val faceMode = cameraController?.getOpenedCameraMode() ?: return null
        val activeArray = CsMediaCamera.getSensorActiveArraySize(faceMode) ?: return null

        if (viewWidth <= 0 || viewHeight <= 0) {
            return null
        }

        val sensorToView = Matrix()
        val sensorRect = RectF(activeArray)
        val viewArea = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        if (!sensorToView.setRectToRect(sensorRect, viewArea, Matrix.ScaleToFit.FILL)) {
            return null
        }

        sensorToView.postConcat(getPreviewDisplayMatrix())
        if (shouldMirrorPreviewHorizontally(faceMode)) {
            sensorToView.postScale(-1f, 1f, viewArea.centerX(), viewArea.centerY())
        }

        val viewToSensor = Matrix()
        if (!sensorToView.invert(viewToSensor)) {
            return null
        }

        val mappedRect = RectF(viewRect)
        viewToSensor.mapRect(mappedRect)
        return mappedRect.toClampedSensorRect(activeArray)
    }

    /**
     * 获取预览内容最终显示到 View 上时使用的矩阵。
     *
     * 子类如果对预览画面做了旋转、缩放等显示变换，需要返回与显示逻辑同源的矩阵，
     * 用于将触摸区域从 View 坐标反算到传感器坐标。
     * */
    protected open fun getPreviewDisplayMatrix(): Matrix {
        return Matrix()
    }

    /**
     * 判断当前预览画面是否做了水平镜像。
     *
     * 如果前置摄像头以镜像方式显示，View 坐标转换到传感器坐标时需要同步水平翻转，
     * 保证用户点击的位置与画面中看到的位置一致。
     * */
    protected open fun shouldMirrorPreviewHorizontally(faceMode: CameraFaceMode): Boolean {
        return false
    }

    /**
     * 将矩形裁剪到传感器 active array 范围内。
     *
     * 如果裁剪后没有有效交集，或者宽高无效，则返回 null，避免向 Camera2 下发非法测光区域。
     * */
    private fun RectF.toClampedSensorRect(activeArray: Rect): Rect? {
        val clamped = RectF(this)
        if (!clamped.intersect(RectF(activeArray))) {
            return null
        }

        val left = clamped.left.toInt()
        val top = clamped.top.toInt()
        val right = clamped.right.toInt()
        val bottom = clamped.bottom.toInt()
        if (right <= left || bottom <= top) {
            return null
        }

        return Rect(left, top, right, bottom)
    }

}
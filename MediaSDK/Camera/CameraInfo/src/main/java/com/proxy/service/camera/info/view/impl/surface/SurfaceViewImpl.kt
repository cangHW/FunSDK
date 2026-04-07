package com.proxy.service.camera.info.view.impl.surface

import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.camera.info.view.base.AbstractCameraSurfaceView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils

/**
 * @author: cangHX
 * @data: 2026/2/5 20:14
 * @desc:
 */
class SurfaceViewImpl(
    config: CameraViewConfig,
    private val view: SurfaceView
) : AbstractCameraSurfaceView(config, view), ICameraView {

    private var holder: SurfaceHolder? = null

    override fun init() {
        super.init()

        view.holder.addCallback(surfaceHolderCallback)
    }

    override fun startCameraPreview() {
        CsLogger.tag(tag)
            .d("startCameraPreview. surfaceWidth=$surfaceWidth, surfaceHeight=$surfaceHeight, rotation=${rotation?.name}")
        val holder = holder ?: return

        if (surfaceWidth <= 0) {
            return
        }

        if (surfaceHeight <= 0) {
            return
        }

        val previewSize = getCalculatePreviewSize(surfaceWidth, surfaceHeight)
        if (previewSize == null) {
            CsLogger.tag(tag).e("没有合适的预览尺寸.")
            return
        }

        adjustSurfaceViewTransform(surfaceWidth, surfaceHeight, previewSize)
        holder.setFixedSize(previewSize.width, previewSize.height)
        cameraController?.setPreviewSurface(holder.surface)

        startPreview()
    }

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        private fun runOnTextureChange(holder: SurfaceHolder, width: Int, height: Int) {
            if (surfaceWidth != width || surfaceHeight != height) {
                surfaceWidth = width
                surfaceHeight = height
                this@SurfaceViewImpl.holder = holder
                rotation = CsScreenUtils.getScreenRotation()

                startCameraPreview()
            }
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            CsLogger.tag(tag).d("surfaceCreated")
            runOnTextureChange(holder, 0, 0)

            startCallScreenRotation()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            CsLogger.tag(tag).d("surfaceChanged. width=$width, height=$height")
            var viewW = view.width
            if (viewW == 0) {
                viewW = width
            }
            var viewH = view.height
            if (viewH == 0) {
                viewH = height
            }

            runOnTextureChange(holder, viewW, viewH)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            CsLogger.tag(tag).d("surfaceDestroyed")
            this@SurfaceViewImpl.holder = null
            surfaceWidth = 0
            surfaceHeight = 0
            rotation = null
            stopPreview()

            finishCallScreenRotation()
        }
    }

    private fun adjustSurfaceViewTransform(viewW: Int, viewH: Int, previewSize: Size) {
        val widthScale = viewW.toFloat() / previewSize.width
        val heightScale = viewH.toFloat() / previewSize.height
        val scale = widthScale.coerceAtLeast(heightScale)

        val rotation = CameraUtils.calculatePreviewRotation()
        CsLogger.tag(tag).i("调整预览旋转角度 rotation = $rotation")

        view.scaleX = scale
        view.scaleY = scale
        view.rotation = rotation.toFloat()
        view.pivotX = viewW / 2f
        view.pivotY = viewH / 2f
    }
}
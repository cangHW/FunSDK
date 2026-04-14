package com.proxy.service.camera.info.view.impl.surface

import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.camera.info.view.base.AbstractCameraSurfaceView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils


/**
 * @author: cangHX
 * @data: 2026/2/5 10:25
 * @desc:
 */
class TextureViewImpl(
    config: CameraViewConfig,
    private val view: TextureView
) : AbstractCameraSurfaceView(config) {

    private var surfaceWidth: Int = 0
    private var surfaceHeight: Int = 0

    private var surface: SurfaceTexture? = null

    override fun init() {
        super.init()
        view.surfaceTextureListener = surfaceTextureListener
    }

    override fun startCameraPreview() {
        CsLogger.tag(tag).d("startCameraPreview.")
        val surface = surface ?: return

        val faceMode = cameraController?.getOpenedCameraMode() ?: return
        val previewSize = getCalculatePreviewSize(faceMode, surfaceWidth, surfaceHeight)
        if (previewSize == null) {
            CsLogger.tag(tag).e("没有合适的预览尺寸.")
            return
        }

        view.setTransform(createTransformImageMatrix(surfaceWidth, surfaceHeight))
        surface.setDefaultBufferSize(previewSize.width, previewSize.height)
        cameraController?.setPreviewSurface(Surface(surface))
        cameraController?.startPreview()
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        private fun runOnTextureChange(surface: SurfaceTexture, width: Int, height: Int) {
            if (surfaceWidth != width || surfaceHeight != height) {
                surfaceWidth = width
                surfaceHeight = height
                this@TextureViewImpl.surface = surface
                rotation = CsScreenUtils.getScreenRotation()

                startCameraPreview()
            }
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            CsLogger.tag(tag).i("onSurfaceTextureAvailable. width=$width, height=$height")
            runOnTextureChange(surface, width, height)

            startCallScreenRotation()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            CsLogger.tag(tag).i("onSurfaceTextureSizeChanged. width=$width, height=$height")
            runOnTextureChange(surface, width, height)
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            CsLogger.tag(tag).i("onSurfaceTextureDestroyed")
            this@TextureViewImpl.surface = null
            surfaceWidth = 0
            surfaceHeight = 0
            rotation = null
            stopPreview()

            finishCallScreenRotation()
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//            CsLogger.tag(tag).i("onSurfaceTextureUpdated")
        }
    }

    private fun createTransformImageMatrix(viewW: Int, viewH: Int): Matrix {
        val matrix = Matrix()
        val textureRectF = RectF(0f, 0f, viewW.toFloat(), viewH.toFloat())

        val centerX = textureRectF.centerX()
        val centerY = textureRectF.centerY()

        if (CsScreenUtils.isLandscape()) {
            val widthScale = textureRectF.height() / textureRectF.width()
            val heightScale = textureRectF.width() / textureRectF.height()
            matrix.postScale(widthScale, heightScale, centerX, centerY)
        }

        val rotation = CameraUtils.calculatePreviewRotation()
        CsLogger.tag(tag).i("调整预览旋转角度 rotation = $rotation")
        matrix.postRotate(rotation.toFloat(), centerX, centerY)
        return matrix
    }
}
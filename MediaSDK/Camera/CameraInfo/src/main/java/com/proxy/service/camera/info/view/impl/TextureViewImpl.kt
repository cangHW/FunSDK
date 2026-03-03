package com.proxy.service.camera.info.view.impl

import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.camera.info.view.base.AbstractCameraActionView
import com.proxy.service.core.framework.data.log.CsLogger


/**
 * @author: cangHX
 * @data: 2026/2/5 10:25
 * @desc:
 */
class TextureViewImpl(
    config: ViewConfig,
    owner: LifecycleOwner?,
    private val view: TextureView
) : AbstractCameraActionView(config, owner) {

    private var _surface: SurfaceTexture? = null
    private var _width: Int = view.width
    private var _height: Int = view.height

    override fun init() {
        super.init()

        view.surfaceTextureListener = surfaceTextureListener
    }

    override fun startPreview() {
        val surface = _surface ?: return

        val previewSize = getCalculatePreviewSize(_width, _height)
        val outSize = getCalculateOutSize(_width, _height)

        if (previewSize == null) {
            CsLogger.tag(tag).e("没有合适的预览尺寸.")
            return
        }

        if (outSize == null) {
            CsLogger.tag(tag).e("没有合适的拍照尺寸.")
            return
        }

        if (mCameraMode == CameraMode.CAPTURE) {
            mCameraLoader.setPictureCaptureSize(outSize.width, outSize.height)
        } else {
            mCameraLoader.setVideoRecordSize(outSize.width, outSize.height)
        }

        view.setTransform(createTransformImageMatrix(_width, _height, previewSize))

        surface.setDefaultBufferSize(previewSize.width, previewSize.height)
        mCameraLoader.setPreviewSurface(Surface(surface), mCameraMode)
    }

    private val surfaceTextureListener = object : SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            _surface = surface
            _width = width
            _height = height

            startPreview()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            onSurfaceTextureAvailable(surface, width, height)
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            _surface = null
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }
    }

    private fun createTransformImageMatrix(viewW: Int, viewH: Int, preferredSize: Size): Matrix {
        val matrix = Matrix()
        val textureRectF = RectF(0f, 0f, viewW.toFloat(), viewH.toFloat())
        val previewRectF = RectF(
            0f,
            0f,
            preferredSize.height.toFloat(),
            preferredSize.width.toFloat()
        )
        val centerX = textureRectF.centerX()
        val centerY = textureRectF.centerY()
        previewRectF.offset(
            centerX - previewRectF.centerX(),
            centerY - previewRectF.centerY()
        )
        matrix.setRectToRect(textureRectF, previewRectF, Matrix.ScaleToFit.FILL)

        val widthScale = viewW.toFloat() / preferredSize.width
        val heightScale = viewH.toFloat() / preferredSize.height

        val scale: Float = widthScale.coerceAtLeast(heightScale)
        matrix.postScale(scale, scale, centerX, centerY)

        val rotate = CameraUtils.calculatePreviewRotation()
        matrix.postRotate(rotate.toFloat(), centerX, centerY)
        return matrix
    }
}
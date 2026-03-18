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
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum


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
    private var _rotation: RotationEnum? = null
    private var _width: Int = 0
    private var _height: Int = 0
    private var _rotationCallbackAdded: Boolean = false

    override fun init() {
        super.init()

        view.surfaceTextureListener = surfaceTextureListener
    }

    override fun startPreview() {
        CsLogger.tag(tag).d("startPreview. _width=$_width, _height=$_height, _rotation=${_rotation?.name}")
        val surface = _surface ?: return

        if (_width <= 0) {
            return
        }

        if (_height <= 0) {
            return
        }

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

        private fun runOnTextureChange(surface: SurfaceTexture, width: Int, height: Int) {
            if (_width != width || _height != height) {
                _width = width
                _height = height
                _surface = surface
                _rotation = CsScreenUtils.getScreenRotation()

                startPreview()
            }
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            CsLogger.tag(tag).i("onSurfaceTextureAvailable. width=$width, height=$height")
            runOnTextureChange(surface, width, height)

            if (!_rotationCallbackAdded) {
                CsScreenUtils.addScreenRotationCallback(screenRotationCallback)
                _rotationCallbackAdded = true
            }
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            CsLogger.tag(tag).i("onSurfaceTextureSizeChanged. width=$width, height=$height")
            runOnTextureChange(surface, width, height)
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            CsLogger.tag(tag).i("onSurfaceTextureDestroyed")
            _surface = null
            _width = 0
            _height = 0
            _rotation = null
            mCameraLoader.pausePreview()
            if (_rotationCallbackAdded) {
                CsScreenUtils.removeScreenRotationCallback(screenRotationCallback)
                _rotationCallbackAdded = false
            }
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//            CsLogger.tag(tag).i("onSurfaceTextureUpdated")
        }
    }

    private val screenRotationCallback = object : ScreenRotationCallback {
        override fun onRotation(rotation: RotationEnum) {
            CsLogger.tag(tag).d("onRotation. rotation=${rotation.name}")
            if (_surface == null) return
            if (_rotation == rotation) {
                return
            }
            _rotation = rotation
            val viewW = if (view.width > 0) view.width else _width
            val viewH = if (view.height > 0) view.height else _height
            if (viewW > 0 && viewH > 0) {
                _width = viewW
                _height = viewH
                startPreview()
            }
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

        val rotation = CameraUtils.calculatePreviewRotation()
        CsLogger.tag(tag).i("调整预览旋转角度 rotation = $rotation")
        matrix.postRotate(rotation.toFloat(), centerX, centerY)
        return matrix
    }
}
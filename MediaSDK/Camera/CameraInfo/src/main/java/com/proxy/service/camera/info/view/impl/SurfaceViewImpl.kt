package com.proxy.service.camera.info.view.impl

import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
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
 * @data: 2026/2/5 20:14
 * @desc:
 */
class SurfaceViewImpl(
    config: ViewConfig,
    owner: LifecycleOwner?,
    private val view: SurfaceView
) : AbstractCameraActionView(config, owner) {

    private var _holder: SurfaceHolder? = null
    private var _rotation: RotationEnum? = null
    private var _width: Int = 0
    private var _height: Int = 0
    private var _rotationCallbackAdded: Boolean = false

    override fun init() {
        super.init()

        view.holder.addCallback(surfaceHolderCallback)
    }

    override fun startPreview() {
        CsLogger.tag(tag).d("startPreview. _width=$_width, _height=$_height, _rotation=${_rotation?.name}")
        val holder = _holder ?: return

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
        // 设置输出尺寸
        if (mCameraMode == CameraMode.CAPTURE) {
            mCameraLoader.setPictureCaptureSize(outSize.width, outSize.height)
        } else {
            mCameraLoader.setVideoRecordSize(outSize.width, outSize.height)
        }
        holder.setFixedSize(previewSize.width, previewSize.height)
        adjustSurfaceViewTransform(_width, _height, previewSize)
        mCameraLoader.setPreviewSurface(holder.surface, mCameraMode)
    }

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            CsLogger.tag(tag).d("surfaceCreated")
            _holder = holder

            if (!_rotationCallbackAdded) {
                CsScreenUtils.addScreenRotationCallback(screenRotationCallback)
                _rotationCallbackAdded = true
            }
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
            if (_width != viewW || _height != viewH) {
                _holder = holder
                _width = viewW
                _height = viewH
                _rotation = CsScreenUtils.getScreenRotation()
                startPreview()
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            CsLogger.tag(tag).d("surfaceDestroyed")
            _holder = null
            _width = 0
            _height = 0
            _rotation = null
            mCameraLoader.pausePreview()
            if (_rotationCallbackAdded) {
                CsScreenUtils.removeScreenRotationCallback(screenRotationCallback)
                _rotationCallbackAdded = false
            }
        }
    }

    private val screenRotationCallback = object : ScreenRotationCallback {
        override fun onRotation(rotation: RotationEnum) {
            CsLogger.tag(tag).d("onRotation. rotation=${rotation.name}")
            if (_holder == null) return
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
package com.proxy.service.camera.info.view.impl

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.SensorOrientationMode
import com.proxy.service.camera.info.view.base.AbstractViewImpl
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils

/**
 * @author: cangHX
 * @data: 2026/2/5 10:25
 * @desc:
 */
class TextureViewImpl(
    private val config: ViewConfig,
    private val owner: LifecycleOwner?,
    private val view: TextureView
) : AbstractViewImpl(config, owner) {

    override fun init() {
        super.init()

        loader.openCamera(cameraFaceMode)
        view.surfaceTextureListener = surfaceTextureListener
    }

    private val surfaceTextureListener = object : SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            val supportSize = cameraService.getSupportedSizes(cameraFaceMode)
            val size = chooseOptimalSize(supportSize, width, height)
            if (size == null) {
                CsLogger.tag(tag).e("没有合适的预览尺寸.")
                return
            }
            surface.setDefaultBufferSize(size.width, size.height)

            if (config.getCameraMode() == CameraMode.PICTURE) {
                loader.setPicturePreview(Surface(surface), size.width, size.height)
            }else{
                loader.setVideoPreview(Surface(surface))
            }

            val displayRotation = CsScreenUtils.getScreenRotation()
            val matrix = Matrix()
            val centerX = view.width / 2f
            val centerY = view.height / 2f
            matrix.postRotate(-displayRotation.degree * 90f, centerX, centerY)
            view.setTransform(matrix)
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            onSurfaceTextureAvailable(surface, width, height)
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }
    }

    private fun chooseOptimalSize(
        supportedSizes: List<Size>,
        viewWidth: Int,
        viewHeight: Int
    ): Size? {
        val maxWidth = 1920
        val maxHeight = 1080

        val aspectRatio = when (cameraService.getSensorOrientation(cameraFaceMode)) {
            SensorOrientationMode.ORIENTATION_0, SensorOrientationMode.ORIENTATION_180 -> {
                viewWidth.toFloat() / viewHeight
            }

            else -> {
                viewHeight.toFloat() / viewWidth
            }
        }
//        val aspectRatio = if (viewWidth > viewHeight) {
//            viewWidth.toFloat() / viewHeight
//        } else {
//            viewHeight.toFloat() / viewWidth
//        }

        var offset = Float.MAX_VALUE
        var finalWidth = -1
        var finalHeight = -1

        supportedSizes.forEach {
            val temp = Math.abs(it.width.toFloat() / it.height - aspectRatio)
            if (temp < offset) {
                offset = temp

                finalWidth = it.width
                finalHeight = it.height
            }
        }

        if (finalWidth == -1 && finalHeight == -1) {
            return null
        }

        return Size(finalWidth, finalHeight)
    }
}
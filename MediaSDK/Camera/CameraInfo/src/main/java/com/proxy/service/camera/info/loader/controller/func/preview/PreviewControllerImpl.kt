package com.proxy.service.camera.info.loader.controller.func.preview

import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import com.proxy.service.camera.base.callback.loader.PreviewCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraPreviewController
import com.proxy.service.camera.base.mode.loader.PreviewImageFormatMode
import com.proxy.service.camera.info.loader.controller.func.base.BaseSurfaceController
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/4/20 11:43
 * @desc:
 */
class PreviewControllerImpl private constructor(
    private val callback: PreviewCallback
) : BaseSurfaceController(), ICameraPreviewController, ImageReader.OnImageAvailableListener {

    companion object {
        private const val TAG = "${CameraConstants.TAG}PreviewController"

        fun create(callback: PreviewCallback): PreviewControllerImpl {
            return PreviewControllerImpl(callback)
        }
    }

    private val handlerThread =
        HandlerThread("PreviewController-${System.currentTimeMillis()}").apply {
            start()
        }
    private val handler = Handler(handlerThread.looper)

    private var imageFormat: Int = ImageFormat.YUV_444_888
    private var reader: ImageReader? = null


    override fun getTag(): String {
        return TAG
    }

    override fun resetSurface() {
        destroy()
    }

    override fun createSurface(): Surface? {
        CsLogger.tag(TAG).i("createSurface.")
        var reader = this.reader
        if (reader == null) {
            reader = ImageReader.newInstance(width, height, imageFormat, 2)
            reader.setOnImageAvailableListener(this, handler)
            this.reader = reader

            funSurfaceChangedCallback?.onSurfaceChanged()
        }
        return reader.surface
    }

    override fun destroy() {
        CsLogger.tag(TAG).i("destroy.")
        try {
            reader?.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        } finally {
            reader = null
        }

        handler.post {
            try {
                handlerThread.quitSafely()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

    override fun setImageFormatMode(format: PreviewImageFormatMode) {
        val currentFormat = when (format) {
            PreviewImageFormatMode.YUV_420_888 -> {
                ImageFormat.YUV_420_888
            }

            PreviewImageFormatMode.YUV_422_888 -> {
                ImageFormat.YUV_422_888
            }

            PreviewImageFormatMode.YUV_444_888 -> {
                ImageFormat.YUV_444_888
            }

            else -> {
                ImageFormat.YUV_420_888
            }
        }

        if (imageFormat == currentFormat) {
            return
        }

        imageFormat = currentFormat
        resetSurface()
        createSurface()
    }


    override fun onImageAvailable(reader: ImageReader?) {
        if (reader == null) {
            return
        }
        var image: Image? = null
        try {
            image = reader.acquireLatestImage()
            callback.onPreviewChanged(image)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        } finally {
            try {
                image?.close()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }
}
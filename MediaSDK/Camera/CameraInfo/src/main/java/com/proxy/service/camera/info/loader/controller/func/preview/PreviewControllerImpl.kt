package com.proxy.service.camera.info.loader.controller.func.preview

import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import com.proxy.service.camera.base.callback.loader.PreviewCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.loader.PreviewImageFormatMode
import com.proxy.service.camera.info.loader.controller.IFunController
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/26 15:54
 * @desc:
 */
class PreviewControllerImpl private constructor(
    private val format: PreviewImageFormatMode,
    private val pWidth: Int,
    private val pHeight: Int,
    private val callback: PreviewCallback
) : IFunController, ImageReader.OnImageAvailableListener {

    companion object {

        private const val TAG = "${CameraConstants.TAG}PreviewController"

        fun create(
            format: PreviewImageFormatMode,
            pWidth: Int,
            pHeight: Int,
            callback: PreviewCallback
        ): PreviewControllerImpl {
            return PreviewControllerImpl(format, pWidth, pHeight, callback)
        }
    }

    private val handlerThread =
        HandlerThread("PreviewController-${System.currentTimeMillis()}").apply {
            start()
        }
    private val handler = Handler(handlerThread.looper)

    private var reader: ImageReader

    private fun parseImageFormat(): Int {
        return when (format) {
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
    }

    init {
        reader = ImageReader.newInstance(pWidth, pHeight, parseImageFormat(), 2)
        reader.setOnImageAvailableListener(this, handler)
    }

    override fun getSurface(): Surface {
        return reader.surface
    }

    override fun setSurfaceChangedCallback(callback: IFunController.SurfaceChangedCallback) {

    }

    override fun setParamsController(controller: IFunController.IParamsController) {

    }

    override fun destroy() {
        try {
            reader.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        handler.post {
            try {
                handlerThread.quitSafely()
                handlerThread.join()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
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
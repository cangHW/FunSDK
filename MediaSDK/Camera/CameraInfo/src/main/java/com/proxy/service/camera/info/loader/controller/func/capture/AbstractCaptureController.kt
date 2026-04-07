package com.proxy.service.camera.info.loader.controller.func.capture

import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.view.Surface
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.info.loader.controller.IFunController
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.nio.ByteBuffer

/**
 * @author: cangHX
 * @data: 2026/3/25 09:51
 * @desc:
 */
abstract class AbstractCaptureController(
    private val handler: Handler
) : ICameraCaptureController, com.proxy.service.camera.info.loader.controller.IFunController, ImageReader.OnImageAvailableListener {

    protected val tag = "${CameraConstants.TAG}CaptureController"

    private var width: Int = 1080
    private var height: Int = 720
    private var reader: ImageReader? = null
    private var surfaceChangedCallback: com.proxy.service.camera.info.loader.controller.IFunController.SurfaceChangedCallback? = null


    override fun setPictureCaptureSize(width: Int, height: Int) {
        if (this.width != width || this.height != height) {
            this.width = width
            this.height = height
            destroy()
            refreshImageReader()
        }
    }

    override fun getSurface(): Surface {
        return refreshImageReader().surface
    }

    override fun setSurfaceChangedCallback(callback: com.proxy.service.camera.info.loader.controller.IFunController.SurfaceChangedCallback) {
        this.surfaceChangedCallback = callback
    }

    override fun destroy() {
        try {
            reader?.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        } finally {
            reader = null
        }
    }

    private fun refreshImageReader(): ImageReader {
        var reader = this.reader
        if (reader == null) {
            reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            reader.setOnImageAvailableListener(this, handler)
            this.reader = reader

            surfaceChangedCallback?.onSurfaceChanged()
        }
        return reader
    }


    override fun onImageAvailable(reader: ImageReader?) {
        if (reader == null) {
            callFailed()
            return
        }
        var image: Image? = null
        try {
            image = reader.acquireLatestImage()
            val buffer: ByteBuffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            CsTask.ioThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    saveImage(bytes)
                    return ""
                }
            })?.start()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
            callFailed()
        } finally {
            try {
                image?.close()
            } catch (throwable: Throwable) {
                CsLogger.tag(tag).e(throwable)
            }
        }
    }

    protected abstract fun saveImage(bytes: ByteArray)

    protected abstract fun callSuccess(filePath: String)

    protected abstract fun callFailed()

}
package com.proxy.service.camera.info.loader.controller.func.capture

import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.view.Surface
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.info.loader.controller.func.base.BaseSurfaceController
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
) : BaseSurfaceController(), ICameraCaptureController, ImageReader.OnImageAvailableListener {

    companion object{
        private const val TAG = "${CameraConstants.TAG}CaptureController"
    }

    protected var reader: ImageReader? = null

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
            reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
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
            CsLogger.tag(TAG).e(throwable)
            callFailed()
        } finally {
            try {
                image?.close()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

    protected abstract fun saveImage(bytes: ByteArray)

    protected abstract fun callSuccess(filePath: String)

    protected abstract fun callFailed()

}
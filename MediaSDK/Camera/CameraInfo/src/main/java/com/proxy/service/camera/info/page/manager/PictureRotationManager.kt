package com.proxy.service.camera.info.page.manager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * @author: cangHX
 * @data: 2026/4/17 18:45
 * @desc:
 */
class PictureRotationManager private constructor(
    private val outPictureCaptureCallback: PictureCaptureCallback?,
    private val outPictureCaptureByteCallback: PictureCaptureByteCallback?
) : PictureCaptureCallback, PictureCaptureByteCallback {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Page_PictureRotationManager"

        fun create(
            callback: PictureCaptureCallback?,
            byteCallback: PictureCaptureByteCallback?
        ): PictureRotationManager {
            return PictureRotationManager(callback, byteCallback)
        }
    }

    private var degree: Float = 0f

    fun setRotation(degree: Float) {
        CsLogger.tag(TAG).d("degree = $degree")
        this.degree = degree
    }

    override fun onPictureCaptureSuccess(filePath: String) {
        if (degree == 0f) {
            outPictureCaptureCallback?.onPictureCaptureSuccess(filePath)
            return
        }

        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                var bitmap: Bitmap? = null
                var rotatedBitmap: Bitmap? = null
                try {
                    bitmap = BitmapFactory.decodeFile(filePath)
                    val matrix = Matrix()
                    matrix.postRotate(degree)
                    rotatedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        matrix,
                        true
                    )
                    FileOutputStream(File(filePath)).use { outputStream ->
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                } finally {
                    bitmap?.recycle()
                    rotatedBitmap?.recycle()
                }
                return ""
            }
        })?.mainThread()?.doOnNext(object : IConsumer<String> {
            override fun accept(value: String) {
                outPictureCaptureCallback?.onPictureCaptureSuccess(filePath)
            }
        })?.start()
    }

    override fun onPictureCaptureSuccess(bytes: ByteArray) {
        if (degree == 0f) {
            outPictureCaptureByteCallback?.onPictureCaptureSuccess(bytes)
            return
        }

        CsTask.ioThread()?.call(object : ICallable<ByteArray> {
            override fun accept(): ByteArray {
                var bitmap: Bitmap? = null
                var rotatedBitmap: Bitmap? = null
                try {
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val matrix = Matrix()
                    matrix.postRotate(degree)
                    rotatedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        matrix,
                        true
                    )
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    return outputStream.toByteArray()
                } finally {
                    bitmap?.recycle()
                    rotatedBitmap?.recycle()
                }
                return ByteArray(0)
            }
        })?.mainThread()?.doOnNext(object : IConsumer<ByteArray> {
            override fun accept(value: ByteArray) {
                if (value.isNotEmpty()) {
                    outPictureCaptureByteCallback?.onPictureCaptureSuccess(value)
                }else{
                    outPictureCaptureByteCallback?.onPictureCaptureFailed()
                }
            }
        })?.start()
    }

    override fun onPictureCaptureFailed() {
        outPictureCaptureCallback?.onPictureCaptureFailed()
        outPictureCaptureByteCallback?.onPictureCaptureFailed()
    }

}
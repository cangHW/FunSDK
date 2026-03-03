package com.proxy.service.camera.info.loader.manager

import android.graphics.ImageFormat
import android.media.ImageReader
import android.os.Handler
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.media.CsFileMediaUtils
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File
import java.nio.ByteBuffer

/**
 * @author: cangHX
 * @data: 2026/2/5 16:28
 * @desc:
 */
class TakePictureController private constructor() {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Capture"

        fun create(): TakePictureController {
            return TakePictureController()
        }
    }

    private var width: Int? = null
    private var height: Int? = null
    private var reader: ImageReader? = null

    private var isSavePhotoAlbum: Boolean = true
    private var callback: TakePictureCallback? = null

    fun isSizeSame(width: Int, height: Int): Boolean {
        return this.width == width && this.height == height
    }

    fun refresh(width: Int, height: Int, handler: Handler) {
        CsLogger.tag(TAG).i("ImageReader. width=$width, height=$height")

        close()
        reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
        this.width = width
        this.height = height
        reader?.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            try {
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
            } finally {
                image.close()
            }
        }, handler)
    }

    fun getImageReader(): ImageReader? {
        return reader
    }

    fun setTakePictureParams(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        this.isSavePhotoAlbum = isSavePhotoAlbum
        this.callback = callback
    }

    fun close() {
        try {
            reader?.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        } finally {
            reader = null
        }
    }

    private fun saveImage(bytes: ByteArray) {
        val file = createFile()
        if (callback?.interceptPhotoSave(bytes, file) == true) {
            if (isSavePhotoAlbum) {
                if (CsFileUtils.isFile(file)) {
                    savePhotoAlbum(file)
                } else {
                    savePhotoAlbum(bytes, file.name)
                }
            } else {
                callback?.onSuccess(file.absolutePath)
            }
            return
        }

        if (isSavePhotoAlbum) {
            savePhotoAlbum(bytes, file.name)
        } else {
            CsFileWriteUtils.setSourceByte(bytes).writeAsync(
                file,
                callback = object : IoCallback {
                    override fun onFailed() {
                        callback?.onFailed()
                    }

                    override fun onSuccess() {
                        callback?.onSuccess(file.absolutePath)
                    }
                }
            )
        }
    }

    private fun savePhotoAlbum(file: File) {
        CsFileMediaUtils.getImageManager()
            .setSourceFile(file)
            .setMimeType(MimeType.IMAGE_JPEG)
            .setDisplayName(file.name)
            .insert(object : InsertCallback {
                override fun onFailed() {
                    callback?.onFailed()
                }

                override fun onSuccess(path: String) {
                    callback?.onSuccess(path)
                }
            })
    }

    private fun savePhotoAlbum(bytes: ByteArray, name: String) {
        CsFileMediaUtils.getImageManager()
            .setSourceByte(bytes)
            .setMimeType(MimeType.IMAGE_JPEG)
            .setDisplayName(name)
            .insert(object : InsertCallback {
                override fun onFailed() {
                    callback?.onFailed()
                }

                override fun onSuccess(path: String) {
                    callback?.onSuccess(path)
                }
            })
    }

    private fun createFile(): File {
        val dir = CsContextManager.getApplication().getExternalFilesDir("camera")
        return File(dir, "photo_${System.currentTimeMillis()}.jpg")
    }
}
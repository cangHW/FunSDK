package com.proxy.service.camera.info.page.manager

import android.text.TextUtils
import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHXa
 * @data: 2026/4/9 17:59
 * @desc:
 */
class PictureCaptureManager private constructor(
    private val params: MediaCameraParams
) {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Page_PictureCaptureManager"

        fun create(params: MediaCameraParams): PictureCaptureManager {
            return PictureCaptureManager(params)
        }
    }

    fun startPictureCapture(cameraCaptureController: ICameraCaptureController?) {
        if (params.pictureCaptureCallback != null) {
            startPictureCaptureFile(cameraCaptureController)
        } else if (params.pictureCaptureByteCallback != null) {
            startPictureCaptureByte(cameraCaptureController)
        }
    }

    private fun startPictureCaptureFile(cameraCaptureController: ICameraCaptureController?) {
        val callback = object : PictureCaptureCallback {
            override fun onPictureCaptureSuccess(filePath: String) {
                callPictureCaptureSuccess(filePath)
            }

            override fun onPictureCaptureFailed() {
                callPictureCaptureFailed()
            }
        }
        if (params.isSaveAlbum) {
            cameraCaptureController?.startPictureCaptureToAlbum(callback)
        } else if (TextUtils.isEmpty(params.filePath)) {
            cameraCaptureController?.startPictureCaptureToLocal(callback)
        } else {
            cameraCaptureController?.startPictureCaptureToLocal(params.filePath ?: "", callback)
        }
    }

    private fun startPictureCaptureByte(cameraCaptureController: ICameraCaptureController?) {
        cameraCaptureController?.startPictureCapture(object : PictureCaptureByteCallback {
            override fun onPictureCaptureSuccess(bytes: ByteArray) {
                callPictureCaptureSuccess(bytes)
            }

            override fun onPictureCaptureFailed() {
                callPictureCaptureFailed()
            }
        })
    }

    private fun callPictureCaptureSuccess(filePath: String) {
        CsLogger.tag(TAG).i("Capture photo success. filePath=$filePath")

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                params.pictureCaptureCallback?.onPictureCaptureSuccess(filePath)
                return ""
            }
        })?.start()
    }

    private fun callPictureCaptureSuccess(bytes: ByteArray) {
        CsLogger.tag(TAG).i("Capture photo success. bytes=${bytes.size}")

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                params.pictureCaptureByteCallback?.onPictureCaptureSuccess(bytes)
                return ""
            }
        })?.start()
    }

    private fun callPictureCaptureFailed() {
        CsLogger.tag(TAG).i("Capture photo failed.")

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                params.pictureCaptureCallback?.onPictureCaptureFailed()
                params.pictureCaptureByteCallback?.onPictureCaptureFailed()
                return ""
            }
        })?.start()
    }
}
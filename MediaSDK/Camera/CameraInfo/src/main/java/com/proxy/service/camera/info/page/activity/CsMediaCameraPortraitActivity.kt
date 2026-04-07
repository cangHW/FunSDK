package com.proxy.service.camera.info.page.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.view.OrientationEventListener
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.permission.base.callback.ActionCallback
import java.io.File
import java.io.FileOutputStream

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
class CsMediaCameraPortraitActivity : CsMediaCameraActivity(), ActionCallback {

    companion object {

        private const val TAG = "${CameraConstants.TAG}PortraitActivity"
    }

    private var orientationListener: OrientationEventListenerImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orientationListener = OrientationEventListenerImpl(this)
        if (orientationListener?.canDetectOrientation() == true) {
            orientationListener?.enable()
        } else {
            CsLogger.tag(TAG).e("传感器不可用, 无法检测方向变化.")
        }
    }

    private var tempPath: String? = null

//    override fun interceptPhotoSave(bytes: ByteArray, file: File): Boolean {
//        tempPath = file.absolutePath
//        var rotation = orientationListener?.getRotation() ?: 0f
//
//        if (cameraFaceMode == CameraFaceMode.FaceBack) {
//            rotation = -rotation
//        }
//
//        rotation = 90 * (rotation - 2)
//
//        if (rotation == 0f) {
//            CsFileWriteUtils.setSourceByte(bytes).writeSync(file)
//            return true
//        }
//
//        val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        val matrix = Matrix()
//        matrix.postRotate(rotation)
//        val rotatedBitmap = Bitmap.createBitmap(
//            originalBitmap,
//            0,
//            0,
//            originalBitmap.width,
//            originalBitmap.height,
//            matrix,
//            true
//        )
//
//        try {
//            FileOutputStream(file).use { outputStream ->
//                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            }
//        } catch (throwable: Throwable) {
//            CsLogger.tag(TAG).e(throwable)
//        }
//        originalBitmap.recycle()
//        rotatedBitmap.recycle()
//        return true
//    }

    override fun onPictureCaptureSuccess(filePath: String) {
        if (tempPath != filePath) {
            CsFileUtils.delete(tempPath)
            tempPath = null
        }
        super.onPictureCaptureSuccess(filePath)
    }


    private class OrientationEventListenerImpl(
        context: Context
    ) : OrientationEventListener(context) {

        private var rotation: Float = 0f

        fun getRotation(): Float {
            return rotation
        }

        override fun onOrientationChanged(orientation: Int) {
            when (orientation) {
                in 45..135 -> {
                    // 横屏（逆时针旋转 90°）
                    CsLogger.tag("onOrientationChanged").e("横屏（逆时针旋转 90°）")
                    rotation = -90f
                }

                in 225..315 -> {
                    // 横屏（顺时针旋转 90°）
                    CsLogger.tag("onOrientationChanged").e("横屏（顺时针旋转 90°）")
                    rotation = 90f
                }

                in 0..45, in 315..360 -> {
                    // 竖屏
                    CsLogger.tag("onOrientationChanged").e("竖屏")
                    rotation = 0f
                }

                in 135..225 -> {
                    // 倒置竖屏
                    CsLogger.tag("onOrientationChanged").e("倒置竖屏")
                    rotation = 180f
                }
            }
        }
    }
}
package com.proxy.service.camera.info.page.activity

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.View
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.ViewMode
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.permission.base.callback.ActionCallback

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
class CsMediaCameraLandscapeActivity : CsMediaCameraActivity() {

    companion object {

        private const val TAG = "${CameraConstants.TAG}Activity"

//        private const val PARAMS = "PARAMS"

//        fun launch(context: Context, params: MediaCameraParams) {
//            val intent = Intent(context, CsMediaCameraLandscapeActivity::class.java)
//            intent.putExtra(PARAMS, params)
//            if (context is Application) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        }
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

    private val takePictureCallback = object : TakePictureCallback {

        private var tempPath: String? = null

//        override fun interceptPhotoSave(bytes: ByteArray, file: File): Boolean {
//            tempPath = file.absolutePath
//            var rotation = orientationListener?.getRotation() ?: 0f
//
//            if (cameraFaceMode == CameraFaceMode.FaceBack) {
//                rotation = -rotation
//            }
//
//            rotation = 90 * (rotation - 2)
//
//            if (rotation == 0f) {
//                CsFileWriteUtils.setSourceByte(bytes).writeSync(file)
//                return true
//            }
//
//            val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//            val matrix = Matrix()
//            matrix.postRotate(rotation)
//            val rotatedBitmap = Bitmap.createBitmap(
//                originalBitmap,
//                0,
//                0,
//                originalBitmap.width,
//                originalBitmap.height,
//                matrix,
//                true
//            )
//
//            try {
//                FileOutputStream(file).use { outputStream ->
//                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                }
//            } catch (throwable: Throwable) {
//                CsLogger.tag(TAG).e(throwable)
//            }
//            originalBitmap.recycle()
//            rotatedBitmap.recycle()
//            return true
//        }

        override fun onSuccess(filePath: String) {
//            if (tempPath != filePath) {
//                CsFileUtils.delete(tempPath)
//                tempPath = null
//            }
//            CsTask.mainThread()?.call(object : ICallable<String> {
//                override fun accept(): String {
//                    params?.takePictureCallback?.onTakePictureSuccess(filePath)
//                    return ""
//                }
//            })?.start()
        }

        override fun onFailed() {
//            CsTask.mainThread()?.call(object : ICallable<String> {
//                override fun accept(): String {
//                    params?.takePictureCallback?.onTakePictureFailed()
//                    return ""
//                }
//            })?.start()
        }
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
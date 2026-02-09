package com.proxy.service.camera.info.page

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.ViewMode
import com.proxy.service.camera.base.view.IView
import com.proxy.service.camera.info.CameraServiceImpl
import com.proxy.service.camera.info.R
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File
import java.io.FileOutputStream

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
class CsMediaCameraActivity : FragmentActivity(), ActionCallback {

    companion object {

        private const val TAG = "${CameraConstants.TAG}Activity"

        private const val PARAMS = "PARAMS"

        fun launch(context: Context, params: MediaCameraParams) {
            val intent = Intent(context, CsMediaCameraActivity::class.java)
            intent.putExtra(PARAMS, params)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val service = CameraServiceImpl()

    private var cameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack

    private var cameraGroup: ViewGroup? = null
    private var iView: IView? = null

    private var orientationListener: OrientationEventListenerImpl? = null

    private var params: MediaCameraParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CsBarUtils.setNavigationBarTransparent(this)
        CsBarUtils.setStatusBarTransparent(this)
        setContentView(R.layout.cs_camera_info_activity_camera)

        params = intent.getSerializableExtra(PARAMS) as? MediaCameraParams?

        cameraGroup = findViewById(R.id.cs_media_camera)

        if (CsPermission.isPermissionGranted(Manifest.permission.CAMERA)) {
            onAction(arrayOf())
        } else {
            CsPermission.createRequest()
                ?.addPermission(Manifest.permission.CAMERA)
                ?.setGrantedCallback(this)
                ?.start()
        }

        findViewById<View>(R.id.capture_photo).setOnClickListener {
            iView?.capturePhoto(true, takePictureCallback)
        }

        findViewById<View>(R.id.change_camera).setOnClickListener {
            cameraFaceMode = if (cameraFaceMode == CameraFaceMode.FaceBack) {
                CameraFaceMode.FaceFront
            } else {
                CameraFaceMode.FaceBack
            }

            iView?.openCamera(cameraFaceMode)
        }

        orientationListener = OrientationEventListenerImpl(this)
        if (orientationListener?.canDetectOrientation() == true) {
            orientationListener?.enable()
        } else {
            CsLogger.tag(TAG).e("传感器不可用, 无法检测方向变化.")
        }
    }

    override fun onAction(list: Array<String>) {
        cameraGroup?.let {
            val config = ViewConfig.builder()
                .setCameraFaceMode(cameraFaceMode)
                .setViewMode(ViewMode.TEXTURE_VIEW)
                .build()
            iView = service.createViewLoader(config)
                .setLifecycleOwner(this)
                .createTo(it)
        }
    }

    private val takePictureCallback = object : TakePictureCallback {

        private var tempPath: String? = null

        override fun interceptPhotoSave(bytes: ByteArray, file: File): Boolean {
            tempPath = file.absolutePath
            var rotation = orientationListener?.getRotation() ?: 0f

            if (cameraFaceMode == CameraFaceMode.FaceBack) {
                rotation = -rotation
            }

            if (rotation == 0f) {
                CsFileWriteUtils.setSourceByte(bytes).writeSync(file)
                return true
            }

            val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val matrix = Matrix()
            matrix.postRotate(rotation)
            val rotatedBitmap = Bitmap.createBitmap(
                originalBitmap,
                0,
                0,
                originalBitmap.width,
                originalBitmap.height,
                matrix,
                true
            )

            try {
                FileOutputStream(file).use { outputStream ->
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            originalBitmap.recycle()
            rotatedBitmap.recycle()
            return true
        }

        override fun onSuccess(filePath: String) {
            if (tempPath != filePath) {
                CsFileUtils.delete(tempPath)
                tempPath = null
            }
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    params?.takePictureCallback?.onTakePictureSuccess(filePath)
                    return ""
                }
            })?.start()
        }

        override fun onFailed() {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    params?.takePictureCallback?.onTakePictureFailed()
                    return ""
                }
            })?.start()
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
                    rotation = -90f
                }

                in 225..315 -> {
                    // 横屏（顺时针旋转 90°）
                    rotation = 90f
                }

                in 0..45, in 315..360 -> {
                    // 竖屏
                    rotation = 0f
                }

                in 135..225 -> {
                    // 倒置竖屏
                    rotation = 180f
                }
            }
        }
    }
}
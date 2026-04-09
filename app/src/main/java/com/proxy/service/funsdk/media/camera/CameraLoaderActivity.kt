package com.proxy.service.funsdk.media.camera

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.loader.camera.ICameraController
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityCameraLoaderBinding
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2026/3/23 18:43
 * @desc:
 */
class CameraLoaderActivity : BaseActivity<ActivityCameraLoaderBinding>() {

    private val tag = "CameraLoaderActivity"

    private var cameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack
    private var iCamera: ICameraController? = null
    private var captureController: ICameraCaptureController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iCamera = CsMediaCamera.createLoader()
            ?.setLifecycleOwner(this)
            ?.createAndOpenCamera(cameraFaceMode)
        captureController = iCamera?.chooseCaptureMode()
        captureController?.setPictureCaptureSize(1440, 1920)
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityCameraLoaderBinding {
        return ActivityCameraLoaderBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        binding?.surfaceTv?.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                iCamera?.setPreviewSurface(Surface(surface))
                iCamera?.startPreview()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                iCamera?.stopPreview()
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.picture_capture -> {
                captureController?.startPictureCaptureToLocal(object : PictureCaptureCallback {
                    override fun onPictureCaptureFailed() {
                        CsToast.show("拍照失败")
                    }

                    override fun onPictureCaptureSuccess(filePath: String) {
                        CsToast.show("拍照成功.")
                        CsLogger.e("filePath=$filePath")
                    }
                })
            }

            R.id.change_camera -> {
                cameraFaceMode = if (cameraFaceMode == CameraFaceMode.FaceBack) {
                    CameraFaceMode.FaceFront
                } else {
                    CameraFaceMode.FaceBack
                }

                iCamera?.openCamera(cameraFaceMode)
                iCamera?.startPreview()
            }
        }
    }
}
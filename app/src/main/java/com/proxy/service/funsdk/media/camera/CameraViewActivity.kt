package com.proxy.service.funsdk.media.camera

import android.view.LayoutInflater
import android.view.View
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.view.CameraViewMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityCameraViewBinding
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2026/3/30 16:48
 * @desc:
 */
class CameraViewActivity : BaseActivity<ActivityCameraViewBinding>() {

    private var cameraView: ICameraView? = null
    private var captureController: ICameraCaptureController? = null

    override fun getViewBinding(inflater: LayoutInflater): ActivityCameraViewBinding {
        return ActivityCameraViewBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        cameraView = CsMediaCamera.createViewLoader()
//            ?.setCameraFaceMode(CameraFaceMode.FaceBack)
            ?.setLifecycleOwner(this)
            ?.setCameraViewMode(CameraViewMode.TEXTURE_VIEW)
            ?.createTo(binding?.cameraLayout)

        captureController = cameraView?.chooseCaptureMode()
        captureController?.setSurfaceSize(1920,1440)
        cameraView?.setPreviewSize(2560, 1920)
        cameraView?.openCamera(CameraFaceMode.FaceBack)
        cameraView?.startPreview()
    }

    override fun onClick(view: View) {
        if (view == binding?.pictureCapture){
            captureController?.startPictureCaptureToLocal(object : PictureCaptureCallback{
                override fun onPictureCaptureFailed() {
                    CsToast.show("拍照失败")
                }

                override fun onPictureCaptureSuccess(filePath: String) {
                    CsToast.show("拍照成功.")
                    CsLogger.e("filePath=$filePath")
                }
            })
        }
    }
}
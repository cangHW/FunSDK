package com.proxy.service.camera.info.page.activity.base

import android.view.LayoutInflater
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.databinding.CsCameraInfoPageActivityCameraBinding
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.widget.info.base.CsBaseActivity

/**
 * @author: cangHX
 * @data: 2026/4/23 17:08
 * @desc:
 */
abstract class AbstractCameraActivity: CsBaseActivity<CsCameraInfoPageActivityCameraBinding>() {

    protected var params: MediaCameraParams = MediaCameraParams()
    protected var cameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack
    private var cameraFunMode: CameraFunMode? = null

    private var iCameraView: ICameraView? = null
    protected var cameraCaptureController: ICameraCaptureController? = null
    protected var cameraRecordController: ICameraRecordController? = null

    override fun isStatusBarDarkModelEnable(): Boolean {
        return true
    }

    override fun getViewBinding(inflater: LayoutInflater): CsCameraInfoPageActivityCameraBinding {
        return CsCameraInfoPageActivityCameraBinding.inflate(inflater)
    }

}
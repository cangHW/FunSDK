package com.proxy.service.camera.info.page.activity.base

import android.Manifest
import android.view.LayoutInflater
import com.proxy.service.camera.base.callback.view.ITouchDispatch
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.base.mode.view.CameraViewAfMode
import com.proxy.service.camera.base.mode.view.CameraViewMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.databinding.CsCameraInfoPageActivityCameraBinding
import com.proxy.service.camera.info.page.activity.CsMediaCameraSettingActivity
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.widget.info.base.CsBaseActivity
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2026/4/23 17:08
 * @desc:
 */
abstract class AbstractCameraActivity : CsBaseActivity<CsCameraInfoPageActivityCameraBinding>() {

    protected var params: MediaCameraParams = MediaCameraParams()
    protected var cameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack
    protected var cameraFunMode: CameraFunMode? = null

    protected var iCameraView: ICameraView? = null
    protected var cameraCaptureController: ICameraCaptureController? = null
    protected var cameraRecordController: ICameraRecordController? = null


    override fun isStatusBarDarkModelEnable(): Boolean {
        return true
    }

    override fun getViewBinding(inflater: LayoutInflater): CsCameraInfoPageActivityCameraBinding {
        return CsCameraInfoPageActivityCameraBinding.inflate(inflater)
    }


    override fun initView() {
        super.initView()
        binding?.cameraSetting?.setOnClickListener {
            CsMediaCameraSettingActivity.launch(this, cameraFaceMode.getCameraId() ?: "")
        }

        binding?.changeCameraFace?.setOnClickListener {
            changeCameraFace()
        }

        binding?.cameraActionIcon?.setOnClickListener {
            actionClick()
        }
    }

    protected open fun changeCameraFace() {
        cameraFaceMode = if (cameraFaceMode == CameraFaceMode.FaceBack) {
            CameraFaceMode.FaceFront
        } else {
            CameraFaceMode.FaceBack
        }

        iCameraView?.openCamera(cameraFaceMode)
    }

    protected open fun requestCamera() {
        if (CsPermission.isPermissionGranted(Manifest.permission.CAMERA)) {
            onCameraPermissionGranted()
        } else {
            CsPermission.createAutoRequest()
                ?.addPermission(Manifest.permission.CAMERA)
                ?.setTitle(getString(R.string.cs_camera_info_page_camera_no_permission))
                ?.setDialogSettingContent(getString(R.string.cs_camera_info_page_camera_request_camera_permission))
                ?.setDialogRationaleContent(getString(R.string.cs_camera_info_page_camera_camera_permission_prompt))
                ?.setGrantedCallback(object : ActionCallback {
                    override fun onAction(list: Array<String>) {
                        onCameraPermissionGranted()
                    }
                })
                ?.setDeniedCallback(object : ActionCallback {
                    override fun onAction(list: Array<String>) {
                        CsToast.show(R.string.cs_camera_info_page_camera_no_camera_permission_toast)
                        finish()
                    }
                })
                ?.start(this)
        }
    }

    protected open fun onCameraPermissionGranted() {
        iCameraView = CsMediaCamera.createViewLoader()
            ?.setCustomTouchDispatch(getTouchDispatch())
            ?.setLifecycleOwner(this)
//            ?.setCameraViewMode(CameraViewMode.TEXTURE_VIEW)
            ?.setCameraViewMode(CameraViewMode.SURFACE_VIEW)
            ?.setCameraFaceMode(cameraFaceMode)
            ?.setCameraViewAfMode(CameraViewAfMode.AfTouchMode())
            ?.createTo(findViewById(R.id.cs_media_camera))
    }

    protected abstract fun actionClick()

    protected abstract fun getTag(): String

    protected abstract fun getTouchDispatch(): ITouchDispatch

}
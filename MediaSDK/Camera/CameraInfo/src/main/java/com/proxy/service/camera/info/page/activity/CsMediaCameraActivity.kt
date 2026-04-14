package com.proxy.service.camera.info.page.activity

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.CameraViewMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.databinding.CsCameraInfoPageActivityCameraBinding
import com.proxy.service.camera.info.page.adapter.CameraModeListAdapter
import com.proxy.service.camera.info.page.cache.CameraParamsCache
import com.proxy.service.camera.info.page.cache.CameraSettingCache
import com.proxy.service.camera.info.page.manager.CameraCustomTouchDispatch
import com.proxy.service.camera.info.page.manager.PictureCaptureManager
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.info.ScreenInfo
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.widget.info.base.CsBaseActivity
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.view.recyclerview.CsCenterSelectRecyclerView

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
open class CsMediaCameraActivity : CsBaseActivity<CsCameraInfoPageActivityCameraBinding>(),
    ActionCallback, CsCenterSelectRecyclerView.OnSelectionChangedListener,
    CameraCustomTouchDispatch.CameraCustomTouchCallback {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Activity"

        const val PARAMS = "PARAMS"
    }

    private var screenInfo: ScreenInfo = CsScreenUtils.getScreenRealInfo()
    private val cameraModeListAdapter = CameraModeListAdapter()

    private var params: MediaCameraParams = MediaCameraParams()
    private var cameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack
    private var cameraFunMode: CameraFunMode? = null

    private var pictureCaptureManager: PictureCaptureManager? = null

    private var iCameraView: ICameraView? = null
    private var cameraCaptureController: ICameraCaptureController? = null
    private var cameraRecordController: ICameraRecordController? = null


    override fun getViewBinding(inflater: LayoutInflater): CsCameraInfoPageActivityCameraBinding {
        return CsCameraInfoPageActivityCameraBinding.inflate(inflater)
    }


    override fun initView() {
        super.initView()

        binding?.cameraSetting?.setOnClickListener {
            CsMediaCameraSettingActivity.launch(this)
        }

        binding?.changeCameraFace?.setOnClickListener {
            cameraFaceMode = if (cameraFaceMode == CameraFaceMode.FaceBack) {
                CameraFaceMode.FaceFront
            } else {
                CameraFaceMode.FaceBack
            }

            iCameraView?.openCamera(cameraFaceMode)
            refreshUiSize()
        }

        binding?.cameraActionIcon?.setOnClickListener {
            if (cameraFunMode == CameraFunMode.CAPTURE) {
                pictureCaptureManager?.startPictureCapture(cameraCaptureController)
            }
        }

        binding?.cameraModeList?.adapter = cameraModeListAdapter
        binding?.cameraModeList?.setOnSelectionChangedListener(this)
    }

    override fun initData(intent: Intent?) {
        super.initData(intent)

        val hashCode = intent?.getIntExtra(PARAMS, 0) ?: -1
        val tempParams = CameraParamsCache.get(hashCode)
        if (tempParams == null) {
            CsToast.show(R.string.cs_camera_info_no_params_toast)
            finish()
            return
        }

        params = tempParams
        pictureCaptureManager = PictureCaptureManager.create(tempParams)

        cameraFaceMode = params.defaultCameraFaceMode
        cameraFunMode = params.supportCameraFunModes.getOrNull(0)

        cameraModeListAdapter.setData(params.supportCameraFunModes)

        if (CsPermission.isPermissionGranted(Manifest.permission.CAMERA)) {
            onAction(arrayOf())
        } else {
            CsPermission.createAutoRequest()
                ?.addPermission(Manifest.permission.CAMERA)
                ?.setTitle(getString(R.string.cs_camera_info_no_permission))
                ?.setDialogSettingContent(getString(R.string.cs_camera_info_request_camera_permission))
                ?.setDialogRationaleContent(getString(R.string.cs_camera_info_camera_permission_prompt))
                ?.setGrantedCallback(this)
                ?.setDeniedCallback(object : ActionCallback {
                    override fun onAction(list: Array<String>) {
                        CsToast.show(R.string.cs_camera_info_no_camera_permission_toast)
                        finish()
                    }
                })
                ?.start(this)
        }
    }


    override fun onAction(list: Array<String>) {
        iCameraView = CsMediaCamera.createViewLoader()
            ?.setCustomTouchDispatch(CameraCustomTouchDispatch.create(this))
            ?.setLifecycleOwner(this)
            ?.setCameraViewMode(CameraViewMode.TEXTURE_VIEW)
            ?.setCameraFaceMode(cameraFaceMode)
            ?.setCameraViewAfMode(CameraViewAfMode.AfTouchMode())
            ?.createTo(findViewById(R.id.cs_media_camera))

        binding?.cameraModeList?.setSelectedPosition(0, false)
    }


    override fun moveLeft() {
        CsLogger.tag(TAG).i("向左滑动")

        binding?.cameraModeList?.setSelectedPosition(1, true)
    }

    override fun moveRight() {
        CsLogger.tag(TAG).i("向右滑动")

        binding?.cameraModeList?.setSelectedPosition(0, true)
    }


    override fun onSelectionChanged(oldPosition: Int, newPosition: Int) {
        CsLogger.tag(TAG)
            .i("onSelectionChanged. oldPosition=$oldPosition, newPosition=$newPosition")

        cameraCaptureController = null
        cameraRecordController = null

        val funMode = params.supportCameraFunModes.getOrNull(newPosition)
        binding?.cameraActionIcon?.setImageResource(funMode?.getModeRes() ?: 0)
        cameraFunMode = funMode

        when (funMode) {
            CameraFunMode.CAPTURE -> {
                cameraCaptureController = iCameraView?.chooseCaptureMode()
            }

            CameraFunMode.RECORD -> {
                cameraRecordController = iCameraView?.chooseRecordMode()
            }

            null -> {
                CsLogger.tag(TAG).e("Unknown type.")
            }
        }

        refreshUiSize()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refreshUiSize()
    }

    private fun refreshUiSize() {
        val view = binding?.csMediaCamera ?: return
        val funMode = cameraFunMode ?: return
        val faceMode = cameraFaceMode

        val size = when (funMode) {
            CameraFunMode.CAPTURE -> {
                CameraSettingCache.getPictureCaptureSize(faceMode).apply {
                    cameraCaptureController?.setPictureCaptureSize(width, height)
                }
            }

            CameraFunMode.RECORD -> {
                CameraSettingCache.getVideoRecordSize(faceMode).apply {
                    cameraRecordController?.setVideoRecordSize(width, height)
                }
            }

            else -> {
                CameraSettingCache.getPreviewSize(faceMode)
            }
        }
        iCameraView?.setPreviewSize(size.width, size.height)
        view.post {
            val w = if (CsScreenUtils.isPortrait() == screenInfo.isPortrait) {
                screenInfo.screenWidth
            } else {
                screenInfo.screenHeight
            }

            val h = if (CsScreenUtils.isPortrait() == screenInfo.isPortrait) {
                screenInfo.screenHeight
            } else {
                screenInfo.screenWidth
            }

            var finalWidth: Int
            var finalHeight: Int

            if (CsScreenUtils.isPortrait()) {
                finalWidth = w
                finalHeight = (w * 1f / size.height * size.width).toInt()
                finalHeight = h.coerceAtMost(finalHeight)
            } else {
                finalHeight = h
                finalWidth = (h * 1f / size.height * size.width).toInt()
                finalWidth = w.coerceAtMost(finalWidth)
            }

            CsLogger.tag(TAG)
                .d("size=$size, w=$w, h=$h, finalWidth=$finalWidth, finalHeight=$finalHeight")

            val params = view.layoutParams
            params.width = finalWidth
            params.height = finalHeight
            view.layoutParams = params
        }
    }
}
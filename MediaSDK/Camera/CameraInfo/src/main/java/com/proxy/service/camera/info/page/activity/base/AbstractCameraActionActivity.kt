package com.proxy.service.camera.info.page.activity.base

import android.content.Intent
import com.proxy.service.camera.base.callback.view.ITouchDispatch
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.page.adapter.CameraModeListAdapter
import com.proxy.service.camera.info.page.cache.CameraParamsCache
import com.proxy.service.camera.info.page.manager.CameraCustomTouchDispatch
import com.proxy.service.camera.info.page.manager.PictureCaptureManager
import com.proxy.service.camera.info.page.manager.VideoRecordManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.view.recyclerview.CsCenterSelectRecyclerView

/**
 * @author: cangHX
 * @data: 2026/4/23 20:22
 * @desc:
 */
abstract class AbstractCameraActionActivity : AbstractSurfaceOrientationActivity(),
    CameraCustomTouchDispatch.CameraCustomTouchCallback,
    CsCenterSelectRecyclerView.OnSelectionChangedListener {

    private val csCameraInfoModeListAdapter = CameraModeListAdapter()

    private var pictureCaptureManager: PictureCaptureManager? = null
    private var videoRecordManager: VideoRecordManager? = null

    override fun getTouchDispatch(): ITouchDispatch {
        return CameraCustomTouchDispatch.create(this)
    }

    override fun initView() {
        super.initView()

        binding?.csCameraInfoModeList?.adapter = csCameraInfoModeListAdapter
        binding?.csCameraInfoModeList?.setOnSelectionChangedListener(this)
    }

    override fun initData(intent: Intent?) {
        super.initData(intent)

        val token = intent?.getStringExtra(CameraParamsCache.CAMERA_TOKEN)
        val tempParams = CameraParamsCache.get(token)
        if (tempParams == null) {
            CsToast.show(R.string.cs_camera_info_page_camera_no_params_toast)
            finish()
            return
        }

        params = tempParams
        pictureCaptureManager = PictureCaptureManager.create(tempParams)
        videoRecordManager = VideoRecordManager.create(tempParams)

        cameraFaceMode = params.defaultCameraFaceMode
        cameraFunMode = params.supportCameraFunModes.getOrNull(0)

        csCameraInfoModeListAdapter.setData(params.supportCameraFunModes)

        requestCamera()
    }

    override fun actionClick() {
        super.actionClick()

        if (cameraFunMode == CameraFunMode.CAPTURE) {
            pictureCaptureManager?.startPictureCapture(cameraCaptureController)
        } else {
            videoRecordManager?.startOrFinishRecord(cameraRecordController)
        }
    }

    override fun onCameraPermissionGranted() {
        super.onCameraPermissionGranted()

        binding?.csCameraInfoModeList?.setSelectedPosition(0, false)
    }


    override fun moveLeft() {
        CsLogger.tag(getTag()).i("向左滑动")

        binding?.csCameraInfoModeList?.setSelectedPosition(1, true)
    }

    override fun moveRight() {
        CsLogger.tag(getTag()).i("向右滑动")

        binding?.csCameraInfoModeList?.setSelectedPosition(0, true)
    }

    override fun onSelectionChanged(oldPosition: Int, newPosition: Int, fromUser: Boolean) {
        CsLogger.tag(getTag())
            .i("onSelectionChanged. oldPosition=$oldPosition, newPosition=$newPosition")

        cameraCaptureController = null
        cameraRecordController = null

        val funMode = params.supportCameraFunModes.getOrNull(newPosition)
        binding?.csCameraInfoActionIcon?.setImageResource(funMode?.getModeRes() ?: 0)
        cameraFunMode = funMode

        when (funMode) {
            CameraFunMode.CAPTURE -> {
                cameraCaptureController = iCameraView?.chooseCaptureMode()
            }

            CameraFunMode.RECORD -> {
                cameraRecordController = iCameraView?.chooseRecordMode()
            }

            null -> {
                CsLogger.tag(getTag()).e("Unknown type.")
            }
        }

        refreshUiSize()
    }

}
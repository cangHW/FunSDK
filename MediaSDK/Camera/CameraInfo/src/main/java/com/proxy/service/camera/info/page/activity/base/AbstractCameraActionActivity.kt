package com.proxy.service.camera.info.page.activity.base

import android.content.Intent
import android.view.View
import com.proxy.service.camera.base.callback.view.ITouchDispatch
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.base.mode.loader.VideoRecordState
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.page.adapter.CameraModeListAdapter
import com.proxy.service.camera.info.page.cache.CameraParamsCache
import com.proxy.service.camera.info.page.manager.CameraCustomTouchDispatch
import com.proxy.service.camera.info.page.manager.PictureCaptureManager
import com.proxy.service.camera.info.page.manager.VideoRecordManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.data.time.enums.TimeIntervalFormat
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IFunction
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.view.recyclerview.CsCenterSelectRecyclerView
import java.util.concurrent.TimeUnit

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
        videoRecordManager = VideoRecordManager.create(tempParams) {
            refreshRecordActionIcon(it)
        }

        cameraFaceMode = params.defaultCameraFaceMode
        cameraFunMode = params.supportCameraFunModes.getOrNull(0)

        csCameraInfoModeListAdapter.setData(params.supportCameraFunModes)

        requestCamera()
    }

    override fun actionClick() {
        super.actionClick()

        if (cameraFunMode == CameraFunMode.CAPTURE) {
            pictureCaptureManager?.startPictureCapture(cameraCaptureController)
        } else if (cameraFunMode == CameraFunMode.RECORD) {
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
        binding?.csCameraInfoRecordTime?.visibility = View.GONE

        cameraFunMode = params.supportCameraFunModes.getOrNull(newPosition)

        when (cameraFunMode) {
            CameraFunMode.CAPTURE -> {
                binding?.csCameraInfoActionIcon?.isSelected = false
                binding?.csCameraInfoActionIcon?.setImageResource(R.drawable.cs_camera_info_camera_capture_photo)
                cameraCaptureController = iCameraView?.chooseCaptureMode()
            }

            CameraFunMode.RECORD -> {
                binding?.csCameraInfoActionIcon?.isSelected = false
                binding?.csCameraInfoActionIcon?.setImageResource(R.drawable.cs_camera_info_camera_record_video_selector)
                cameraRecordController = iCameraView?.chooseRecordMode()
            }

            null -> {
                CsLogger.tag(getTag()).e("Unknown type.")
            }
        }

        refreshUiSize()
    }

    private var recordTimeTask: ITaskDisposable? = null

    private fun refreshRecordActionIcon(state: VideoRecordState) {
        if (cameraFunMode != CameraFunMode.RECORD) {
            return
        }

        CsLogger.tag(getTag()).e("refreshRecordActionIcon state=$state")

        binding?.csCameraInfoActionIcon?.isSelected = when (state) {
            VideoRecordState.STATE_STARTING -> {
                true
            }

            VideoRecordState.STATE_RECORDING -> {
                binding?.csCameraInfoRecordTime?.text = "00:00"
                binding?.csCameraInfoRecordTime?.visibility = View.VISIBLE
                recordTimeTask = CsTask.interval(0, 1, TimeUnit.SECONDS)
                    ?.map(object : IFunction<Long, String> {
                        override fun apply(value: Long): String {
                            if (value <= 0L) {
                                return "00:00"
                            }
                            val interval = CsTimeManager
                                .createIntervalFactory(value * CsTimeManager.SECONDS)
                            if (value < CsTimeManager.HOURS) {
                                return interval.get("MM:SS")
                            }

                            if (value < CsTimeManager.DAYS) {
                                return interval.get("HH:MM:SS")
                            }

                            return interval.get("DD:HH:MM:SS")
                        }
                    })
                    ?.mainThread()
                    ?.doOnNext(object : IConsumer<String> {
                        override fun accept(value: String) {
                            binding?.csCameraInfoRecordTime?.text = value
                        }
                    })
                    ?.start()
                true
            }

            VideoRecordState.STATE_STOPPING -> {
                recordTimeTask?.dispose()
                true
            }

            VideoRecordState.STATE_IDLE -> {
                binding?.csCameraInfoRecordTime?.visibility = View.GONE
                false
            }
        }
    }

}
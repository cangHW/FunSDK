package com.proxy.service.camera.info.page.activity

import android.Manifest
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.proxy.service.camera.base.callback.CustomTouchDispatch
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.ViewMode
import com.proxy.service.camera.base.view.IView
import com.proxy.service.camera.info.CameraServiceImpl
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.page.adapter.CameraModeListAdapter
import com.proxy.service.camera.info.page.cache.CameraCache
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.view.recyclerview.CsCenterSelectRecyclerView
import com.proxy.service.widget.info.view.recyclerview.adapter.CsBaseRecyclerViewAdapter
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
open class CsMediaCameraActivity : FragmentActivity(), ActionCallback, TakePictureCallback,
    CsCenterSelectRecyclerView.OnSelectionChangedListener,
    CsBaseRecyclerViewAdapter.OnItemClickListener<CameraMode> {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Activity"

        const val PARAMS = "PARAMS"
    }

    protected val service = CameraServiceImpl()
    protected var params: MediaCameraParams = MediaCameraParams()
    protected var cameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack
    protected var iView: IView? = null

    private var cameraModeList: CsCenterSelectRecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CsBarUtils.setNavigationBarTransparent(this)
        CsBarUtils.setStatusBarTransparent(this)
        setContentView(R.layout.cs_camera_info_page_activity_camera)

        val code = intent.getIntExtra(PARAMS, 0)
        val tempParams = CameraCache.get(code)
        if (tempParams == null) {
            CsToast.show(R.string.cs_camera_info_no_params_toast)
            finish()
            return
        }

        params = tempParams
        cameraFaceMode = params.defaultCameraFaceMode

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

        findViewById<View>(R.id.change_camera_face).setOnClickListener {
            cameraFaceMode = if (cameraFaceMode == CameraFaceMode.FaceBack) {
                CameraFaceMode.FaceFront
            } else {
                CameraFaceMode.FaceBack
            }

            iView?.openCamera(cameraFaceMode)
        }

        cameraModeList = findViewById(R.id.camera_mode_list)
        val adapter = CameraModeListAdapter()
        adapter.setData(params.supportCameraModes)
        adapter.setOnCameraModeClickListener(this)
        cameraModeList?.adapter = adapter

        cameraModeList?.setOnSelectionChangedListener(this)
        cameraModeList?.setSelectedPosition(0, false)
    }

    override fun onAction(list: Array<String>) {
        val viewGroup = findViewById<ViewGroup>(R.id.cs_media_camera)
        val config = ViewConfig.builder()
            .setCameraFaceMode(cameraFaceMode)
            .setCameraMode(CameraMode.PICTURE)
            .setCameraViewAfMode(CameraViewAfMode.AfTouchMode())
            .setViewMode(ViewMode.TEXTURE_VIEW)
            .build()
        iView = service.createViewLoader(config)
            .setCustomTouchDispatch(customTouchDispatch)
            .setLifecycleOwner(this)
            .createTo(viewGroup)
    }

    private val customTouchDispatch = object : CustomTouchDispatch() {

        private val min_scroll_slop = CsDpUtils.dp2pxf(10f)

        private val isDown = AtomicBoolean(false)
        private var moveX: Float = 0f
        private var moveY: Float = 0f

        private fun reset() {
            moveX = 0f
            moveY = 0f

            isDown.set(false)
        }

        override fun onViewDown(e: MotionEvent) {
            super.onViewDown(e)

            reset()
            isDown.set(true)
        }

        override fun onViewScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ) {
            if (!isDown.get()) {
                return
            }

            if (e2.pointerCount > 1) {
                reset()
                return
            }

            moveX += distanceX
            moveY += distanceY

            val absX = abs(moveX)

            if (abs(moveY) > absX) {
                reset()
                return
            }

            if (absX > min_scroll_slop) {
                if (isDown.compareAndSet(true, false)) {
                    if (moveX > 0) {
                        moveLeft()
                    } else {
                        moveRight()
                    }
                }
            }
        }
    }

    override fun onSuccess(filePath: String) {
        CsLogger.tag(TAG).i("Capture photo success. filePath=$filePath")

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                params.takePictureCallback?.onTakePictureSuccess(filePath)
                return ""
            }
        })?.start()
    }

    override fun onFailed() {
        CsLogger.tag(TAG).i("Capture photo failed.")

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                params.takePictureCallback?.onTakePictureFailed()
                return ""
            }
        })?.start()
    }

    open fun moveLeft() {
        CsLogger.tag(TAG).i("向左滑动")

        cameraModeList?.setSelectedPosition(1, true)
    }

    open fun moveRight() {
        CsLogger.tag(TAG).i("向右滑动")

        cameraModeList?.setSelectedPosition(0, true)
    }

    override fun onSelectionChanged(oldPosition: Int, newPosition: Int) {
        CsLogger.tag(TAG)
            .i("onSelectionChanged. oldPosition=$oldPosition, newPosition=$newPosition")

        params.supportCameraModes.getOrNull(newPosition)?.let {
            iView?.setCameraMode(it)
        }
    }

    override fun onItemClick(data: CameraMode) {
        if (data == CameraMode.PICTURE) {
            iView?.takePicture(false, this)
        }
    }
}
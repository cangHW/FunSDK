package com.proxy.service.camera.info.view.base

import android.graphics.RectF
import android.view.MotionEvent
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo
import com.proxy.service.camera.base.view.IView
import com.proxy.service.camera.info.CameraServiceImpl
import com.proxy.service.camera.info.loader.CameraLoaderImpl
import com.proxy.service.camera.info.view.touch.CameraTouchView
import com.proxy.service.camera.info.view.touch.mode.AfModeDispatch

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractViewImpl(
    config: ViewConfig,
    private val owner: LifecycleOwner?
) : IView, AfModeDispatch.OnCameraAfIntercept {

    protected val tag = "${CameraConstants.TAG}View"

    protected val mCameraService = CameraServiceImpl()
    private var mCameraViewAfMode: CameraViewAfMode = config.getCameraViewAfMode()
    protected val mCameraLoader: ICameraLoader = CameraLoaderImpl(
        LoaderConfig.builder().apply {
            config.getCameraViewAfMode().toCameraAfMode()?.let {
                setCameraAfMode(it)
            }
        }.build()
    )

    protected var mCameraMode: CameraMode = config.getCameraMode()
    protected var mCameraFaceMode: CameraFaceMode = config.getCameraFaceMode()

    /**
     * 初始化
     * */
    open fun init() {
        mCameraLoader.openCamera(mCameraFaceMode)
        owner?.let {
            mCameraLoader.setLifecycleOwner(it)
        }
    }

    /**
     * 开始预览
     * */
    protected abstract fun startPreview()

    /**
     * 检测是否触发手动对焦
     * */
    override fun onTouchAfIntercept(event: MotionEvent): RectF? {
        val mode = mCameraViewAfMode as? CameraViewAfMode.AfTouchMode? ?: return null

        val halfW = mode.width
        val halfH = mode.height

        val x = event.x - halfW
        val y = event.y - halfH

        val list = ArrayList<FocusAreaInfo>()
        list.add(
            FocusAreaInfo.create(
                x.toInt(),
                y.toInt(),
                mode.width,
                mode.height,
                FocusAreaInfo.WEIGHT_MAX
            )
        )
        mCameraLoader.setCameraAfMode(CameraAfMode.AfFixedMode(list))

        return RectF(x, y, event.x + halfW, event.y + halfH)
    }

    override fun setCameraMode(mode: CameraMode) {
        if (mCameraMode == mode) {
            return
        }
        mCameraMode = mode
        startPreview()
    }

    override fun setCameraViewAfMode(mode: CameraViewAfMode) {
        mCameraViewAfMode = mode
        mode.toCameraAfMode()?.let {
            mCameraLoader.setCameraAfMode(it)
        }
    }

    override fun takePicture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        mCameraLoader.takePicture(isSavePhotoAlbum, callback)
    }

    override fun openCamera(mode: CameraFaceMode) {
        mCameraLoader.openCamera(mode)
    }

    override fun pausePreview() {
        mCameraLoader.pausePreview()
    }

    override fun resumePreview() {
        mCameraLoader.resumePreview()
    }

    override fun releaseCamera() {
        mCameraLoader.releaseCamera()
    }

}
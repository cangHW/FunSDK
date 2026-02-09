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
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo
import com.proxy.service.camera.base.view.IView
import com.proxy.service.camera.info.CameraServiceImpl
import com.proxy.service.camera.info.loader.CameraLoaderImpl
import com.proxy.service.camera.info.view.view.TouchView

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractViewImpl(
    config: ViewConfig,
    private val owner: LifecycleOwner?
) : IView, TouchView.OnCameraTouchAfIntercept {

    protected val tag = "${CameraConstants.TAG}View"

    protected val cameraService = CameraServiceImpl()
    private var cameraViewAfMode: CameraViewAfMode = config.getCameraViewAfMode()
    protected val loader: ICameraLoader = CameraLoaderImpl(
        LoaderConfig.builder().apply {
            config.getCameraViewAfMode().toCameraAfMode()?.let {
                setCameraAfMode(it)
            }
        }.build()
    )

    protected var cameraFaceMode: CameraFaceMode = config.getCameraFaceMode()

    /**
     * 初始化
     * */
    open fun init() {
        owner?.let {
            loader.setLifecycleOwner(it)
        }
    }

    /**
     * 检测是否触发手动对焦
     * */
    override fun onTouchAfIntercept(event: MotionEvent): RectF? {
        val mode = cameraViewAfMode as? CameraViewAfMode.AfTouchMode? ?: return null

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
        loader.setCameraAfMode(CameraAfMode.AfFixedMode(list))

        return RectF(x, y, event.x + halfW, event.y + halfH)
    }

    override fun setCameraViewAfMode(mode: CameraViewAfMode) {
        cameraViewAfMode = mode
        mode.toCameraAfMode()?.let {
            loader.setCameraAfMode(it)
        }
    }

    override fun capturePhoto(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        loader.capturePhoto(isSavePhotoAlbum, callback)
    }

    override fun openCamera(mode: CameraFaceMode) {
        loader.openCamera(mode)
    }

    override fun pausePreview() {
        loader.pausePreview()
    }

    override fun resumePreview() {
        loader.resumePreview()
    }

    override fun releaseCamera() {
        loader.releaseCamera()
    }

}
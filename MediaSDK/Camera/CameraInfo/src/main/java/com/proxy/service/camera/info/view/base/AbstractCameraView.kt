package com.proxy.service.camera.info.view.base

import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.CameraServiceImpl
import com.proxy.service.camera.info.loader.CameraLoaderImpl

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractCameraView(
    config: ViewConfig
) : ICameraView {

    protected val tag = "${CameraConstants.TAG}View"

    protected val mCameraService = CameraServiceImpl()
    protected var mCameraViewAfMode: CameraViewAfMode = config.getCameraViewAfMode()
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
     * 开始预览
     * */
    protected abstract fun startPreview()


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


    override fun setPictureCaptureSize(width: Int, height: Int) {
        mCameraLoader.setPictureCaptureSize(width, height)
    }

    override fun startPictureCapture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?) {
        mCameraLoader.startPictureCapture(isSavePhotoAlbum, callback)
    }


    override fun openCamera(mode: CameraFaceMode) {
        mCameraLoader.openCamera(mode)
        startPreview()
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
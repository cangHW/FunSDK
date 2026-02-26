package com.proxy.service.camera.info.view.impl

import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.view.base.AbstractViewImpl

/**
 * @author: cangHX
 * @data: 2026/2/5 20:14
 * @desc:
 */
class SurfaceViewImpl(
    private val config: ViewConfig,
    private val owner: LifecycleOwner?,
    private val view: SurfaceView
) : AbstractViewImpl(config, owner) {

    override fun init() {
        super.init()

        view.holder.addCallback(surfaceHolderCallback)
    }

    override fun startPreview() {

    }

    private val surfaceHolderCallback=object :SurfaceHolder.Callback{
        override fun surfaceCreated(holder: SurfaceHolder) {

//            loader.setPreviewSurface(holder.surface, view.width, view.height)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            if (config.getCameraMode() == CameraMode.PICTURE) {
                mCameraLoader.setPicturePreview(holder.surface, width, height)
            }else{
                mCameraLoader.setVideoPreview(holder.surface)
            }


//            val sensorOrientation = loader.getSensorOrientation(cameraFaceMode)
//            val displayRotation = CsScreenUtils.getScreenRotation()
//            val isFrontCamera = (cameraFaceMode == CameraFaceMode.FaceFront)
//            val rotation = CameraUtils.calculatePreviewRotation(
//                sensorOrientation,
//                displayRotation,
//                isFrontCamera
//            )
//
//            view.rotation = rotation * 1f
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {

        }
    }
}
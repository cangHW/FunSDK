package com.proxy.service.camera.info.loader

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.callback.CameraLoaderCallback
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraController
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.info.loader.camera.CameraControllerImpl
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.camera.info.loader.lifecycle.LifecycleObserverImpl

/**
 * @author: cangHX
 * @data: 2026/3/19 19:59
 * @desc:
 */
class CameraLoaderImpl : ICameraLoader {

    companion object {
        private const val TAG = "${CameraConstants.TAG}Loader"
    }

    private var lifecycleOwner: LifecycleOwner? = null
    private var cameraAfMode: CameraAfMode = CameraConstants.DEFAULT_CAMERA_AF_MODE

    override fun setCameraLoaderCallback(callback: CameraLoaderCallback): ICameraLoader {
        return this
    }

    override fun setLifecycleOwner(owner: LifecycleOwner): ICameraLoader {
        CsLogger.tag(TAG).i("bindLifecycleOwner. owner=$owner")
        this.lifecycleOwner = owner
        return this
    }

    override fun setCameraAfMode(mode: CameraAfMode): ICameraLoader {
        CsLogger.tag(TAG).i("setCameraAfMode. mode=$mode")
        this.cameraAfMode = mode
        return this
    }

    override fun createAndOpenCamera(mode: CameraFaceMode): ICameraController {
        CsLogger.tag(TAG).i("openCamera. mode=$mode")
        val controller = createController()
        controller.openCamera(mode)
        return controller
    }

    override fun createCamera(): ICameraController {
        CsLogger.tag(TAG).i("createCamera.")
        return createController()
    }


    private fun createController(): CameraControllerImpl {
        val controller = CameraControllerImpl()

        lifecycleOwner?.lifecycle?.addObserver(LifecycleObserverImpl(controller))
        controller.setCameraAfMode(cameraAfMode)

        return controller
    }
}
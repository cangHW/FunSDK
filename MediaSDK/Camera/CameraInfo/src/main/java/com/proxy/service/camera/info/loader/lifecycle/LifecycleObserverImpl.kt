package com.proxy.service.camera.info.loader.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.camera.ICameraAction
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/24 10:26
 * @desc:
 */
class LifecycleObserverImpl(
    private val iCameraAction: ICameraAction
) : DefaultLifecycleObserver {

    private val tag = "${CameraConstants.TAG}Lifecycle"

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        CsLogger.tag(tag).d("onResume")

        try {
            iCameraAction.resumePreview()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        CsLogger.tag(tag).d("onPause")

        try {
            iCameraAction.pausePreview()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        CsLogger.tag(tag).d("onDestroy")

        try {
            owner.lifecycle.removeObserver(this)
            iCameraAction.releaseCamera()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

}
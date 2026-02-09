package com.proxy.service.camera.info.loader.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.ICameraLoader
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/6 14:48
 * @desc:
 */
class LifecycleObserverImpl(
    private val owner: LifecycleOwner,
    private val loader: ICameraLoader
) : DefaultLifecycleObserver {

    private val tag = "${CameraConstants.TAG}Lifecycle"

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        CsLogger.tag(tag).d("onResume")
        try {
            loader.resumePreview()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        CsLogger.tag(tag).d("onStop")
        try {
            loader.pausePreview()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        CsLogger.tag(tag).d("onDestroy")
        try {
            owner.lifecycle.removeObserver(this)
            loader.releaseCamera()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

}
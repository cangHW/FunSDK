package com.proxy.service.camera.info.loader

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.info.loader.lifecycle.LifecycleObserverImpl
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/4 16:19
 * @desc:
 */
class CameraLoaderImpl(
    private val config: LoaderConfig
) : AbstractCameraPictureLoader(config) {

    override fun setLifecycleOwner(owner: LifecycleOwner) {
        CsLogger.tag(tag).i("setLifecycleOwner. owner=$owner")
        owner.lifecycle.addObserver(LifecycleObserverImpl(owner, this))
    }

}
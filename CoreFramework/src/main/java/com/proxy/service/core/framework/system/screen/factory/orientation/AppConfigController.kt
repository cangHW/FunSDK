package com.proxy.service.core.framework.system.screen.factory.orientation

import android.content.res.Configuration
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractAppConfigStateChanged
import com.proxy.service.core.framework.system.screen.factory.base.AbstractController

/**
 * @author: cangHX
 * @data: 2026/4/2 10:43
 * @desc:
 */
class AppConfigController private constructor(
    private val callback: OrientationChangedCallback
) : AbstractController() {

    companion object {
        fun create(callback: OrientationChangedCallback): AppConfigController {
            return AppConfigController(callback)
        }
    }

    override fun onInit() {

    }

    override fun onStart() {
        CsContextManager.addAppConfigStateCallback(appConfigStateChanged)
        callback.onOrientationChanged(null)
    }

    override fun onStop() {
        CsContextManager.removeAppConfigStateCallback(appConfigStateChanged)
    }

    private val appConfigStateChanged = object : AbstractAppConfigStateChanged() {
        override fun onConfigurationChanged(newConfig: Configuration) {
            callback.onOrientationChanged(newConfig)
        }
    }
}
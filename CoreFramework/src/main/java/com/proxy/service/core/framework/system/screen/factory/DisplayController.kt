package com.proxy.service.core.framework.system.screen.factory

import android.content.res.Configuration
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractAppStateChanged
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenOrientationCallback
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.callback.base.BaseDisplayCallback
import com.proxy.service.core.framework.system.screen.enums.OrientationEnum
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/5/23 17:44
 * @desc:
 */
class DisplayController private constructor() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Display"

        val instance by lazy {
            DisplayController()
        }
    }

    private var rotation: RotationEnum? = null
    private var orientation: OrientationEnum? = null


    private val appStateChanged = object : AbstractAppStateChanged() {
        override fun onConfigurationChanged(newConfig: Configuration) {
            callbacks.forEachSync {
                if (it is ScreenRotationCallback) {
                    callRotation(it)
                } else if (it is ScreenOrientationCallback) {
                    callOrientation(it)
                }
            }
        }
    }

    init {
        CsContextManager.addAppStateChangedCallback(appStateChanged)

        this.rotation = CsScreenUtils.getScreenRotation(CsContextManager.getTopActivity())
        this.orientation = getScreenOrientationEnum()
    }

    private val callbacks: ISet<BaseDisplayCallback> = CsExcellentSet()

    fun addCallback(callback: BaseDisplayCallback) {
        callbacks.putSync(callback)
    }

    fun removeCallback(callback: BaseDisplayCallback) {
        callbacks.removeSync(callback)
    }


    private fun callRotation(callback: ScreenRotationCallback) {
        val rotation = CsScreenUtils.getScreenRotation(CsContextManager.getTopActivity())
        if (this.rotation == rotation) {
            return
        }
        this.rotation = rotation

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onRotation(rotation)
                return ""
            }
        })?.start()
    }

    private fun callOrientation(callback: ScreenOrientationCallback) {
        val orientation = getScreenOrientationEnum()
        if (this.orientation == orientation) {
            return
        }
        this.orientation = orientation

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onOrientationChange(orientation)
                return ""
            }
        })?.start()
    }


    private fun getScreenOrientationEnum(): OrientationEnum {
        if (CsScreenUtils.isLandscape()) {
            return OrientationEnum.LANDSCAPE
        }
        return OrientationEnum.PORTRAIT
    }
}
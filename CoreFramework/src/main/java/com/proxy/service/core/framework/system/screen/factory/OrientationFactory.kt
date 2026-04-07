package com.proxy.service.core.framework.system.screen.factory

import android.content.res.Configuration
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.collections.type.Type
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenOrientationCallback
import com.proxy.service.core.framework.system.screen.enums.OrientationEnum
import com.proxy.service.core.framework.system.screen.factory.base.AbstractController
import com.proxy.service.core.framework.system.screen.factory.orientation.AppConfigController
import com.proxy.service.core.framework.system.screen.factory.orientation.OrientationChangedCallback
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/5/23 17:44
 * @desc:
 */
class OrientationFactory private constructor() : OrientationChangedCallback {

    companion object {
        private const val TAG = "${CoreConfig.TAG}ScreenOrientation"

        val instance by lazy {
            OrientationFactory()
        }
    }


    private var controller: AbstractController? = null
    private val callbacks: ISet<ScreenOrientationCallback> = CsExcellentSet(Type.WEAK)

    private var orientation: OrientationEnum? = null


    init {
        controller = AppConfigController.create(this)
    }

    fun addWeakCallback(callback: ScreenOrientationCallback) {
        CsLogger.tag(TAG).d("addWeakCallback. callback=$callback")
        callbacks.putSync(callback)

        if (callbacks.size() > 0) {
            controller?.start()
        }
    }

    fun removeCallback(callback: ScreenOrientationCallback) {
        CsLogger.tag(TAG).d("removeCallback. callback=$callback")
        callbacks.removeSync(callback)

        if (callbacks.size() <= 0) {
            orientation = null
            controller?.stop()
        }
    }


    override fun onOrientationChanged(newConfig: Configuration?) {
        val currentOrientation = if (newConfig == null) {
            getScreenOrientationEnum()
        } else {
            if (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) {
                OrientationEnum.LANDSCAPE
            } else {
                OrientationEnum.PORTRAIT
            }
        }

        CsLogger.tag(TAG).d("onOrientationChanged. currentOrientation=${currentOrientation.name}")

        if (orientation == currentOrientation) {
            return
        }
        orientation = currentOrientation

        callbacks.forEachAsync {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    it.onOrientationChange(currentOrientation)
                    return ""
                }
            })?.start()
        }
    }

    private fun getScreenOrientationEnum(): OrientationEnum {
        if (CsScreenUtils.isLandscape()) {
            return OrientationEnum.LANDSCAPE
        }
        return OrientationEnum.PORTRAIT
    }
}
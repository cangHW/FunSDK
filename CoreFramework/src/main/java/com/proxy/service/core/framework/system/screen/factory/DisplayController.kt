package com.proxy.service.core.framework.system.screen.factory

import android.content.Context
import android.hardware.display.DisplayManager
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.data.log.CsLogger
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
        private val THREAD_NAME = "$TAG-${System.currentTimeMillis()}"

        val instance by lazy {
            DisplayController()
        }
    }

    private var handler = CsTask.launchTaskGroup(THREAD_NAME)

    private var displayManager: DisplayManager? = null

    private var rotation: Int? = null
    private var orientation: OrientationEnum? = null


    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) {
            CsLogger.tag(TAG).d("onDisplayAdded displayId = $displayId")
        }

        override fun onDisplayRemoved(displayId: Int) {
            CsLogger.tag(TAG).d("onDisplayRemoved displayId = $displayId")
        }

        override fun onDisplayChanged(displayId: Int) {
            CsLogger.tag(TAG).d("onDisplayChanged displayId = $displayId")

            handler?.start {
                val display = displayManager?.getDisplay(displayId) ?: return@start
                val rotation: Int = display.rotation
                CsLogger.tag(TAG).d("onDisplayChanged rotation = $rotation")

                callbacks.forEachSync {
                    if (it is ScreenRotationCallback) {
                        callRotation(it, rotation)
                    } else if (it is ScreenOrientationCallback) {
                        callOrientation(it, rotation)
                    }
                }
            }
        }
    }

    init {
        displayManager = CsContextManager.getApplication()
            .getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager?
        displayManager?.registerDisplayListener(displayListener, null)

        val rotation = CsScreenUtils.getScreenRotation()
        this.rotation = rotation.degree
        this.orientation = OrientationEnum.valueOf(rotation)
    }

    private val callbacks: ISet<BaseDisplayCallback> = CsExcellentSet()

    fun addCallback(callback: BaseDisplayCallback) {
        callbacks.putSync(callback)
    }

    fun removeCallback(callback: BaseDisplayCallback) {
        callbacks.removeSync(callback)
    }


    private fun callRotation(callback: ScreenRotationCallback, rotation: Int) {
        if (this.rotation == rotation) {
            return
        }
        this.rotation = rotation

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                RotationEnum.valueOf(rotation)?.let {
                    callback.onRotation(it)
                }
                return ""
            }
        })?.start()
    }

    private fun callOrientation(callback: ScreenOrientationCallback, rotation: Int) {
        val orientation = OrientationEnum.valueOf(rotation)
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

}
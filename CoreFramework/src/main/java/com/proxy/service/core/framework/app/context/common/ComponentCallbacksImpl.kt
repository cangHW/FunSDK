package com.proxy.service.core.framework.app.context.common

import android.content.ComponentCallbacks
import android.content.res.Configuration
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.callback.AbstractAppStateChanged
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/9/15 15:45
 * @desc:
 */
class ComponentCallbacksImpl : AbstractAppStateChanged(), ComponentCallbacks {

    companion object {
        private const val TAG = "${CoreConfig.TAG}AppStateChanged"
        private val mInstance by lazy { ComponentCallbacksImpl() }

        fun getInstance(): ComponentCallbacksImpl {
            return mInstance
        }
    }

    private val appStateChangedSet: ISet<AbstractAppStateChanged> = CsExcellentSet()

    fun addAppStateChanged(callback: AbstractAppStateChanged) {
        appStateChangedSet.putSync(callback)
    }

    fun removeAppStateChanged(callback: AbstractAppStateChanged) {
        appStateChangedSet.removeSync(callback)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        appStateChangedSet.forEachAsync {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    it.onConfigurationChanged(newConfig)
                    return ""
                }
            })?.start()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()

        appStateChangedSet.forEachAsync {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    it.onLowMemory()
                    return ""
                }
            })?.start()
        }
    }

}
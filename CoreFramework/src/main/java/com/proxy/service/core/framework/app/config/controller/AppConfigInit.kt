package com.proxy.service.core.framework.app.config.controller

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import com.proxy.service.core.framework.app.config.CsConfigUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.app.context.callback.AbstractAppStateChanged
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/12/24 18:21
 * @desc:
 */
object AppConfigInit {

    private val isInit = AtomicBoolean(false)

    fun init() {
        if (!CsConfigUtils.isConfigHasChange()) {
            return
        }
        if (isInit.compareAndSet(false, true)) {
            CsContextManager.addActivityLifecycleCallback(null, true, activityLifecycle)
            CsContextManager.addAppStateChangedCallback(appStateChanged)
        }
    }

    fun finish() {
        if (CsConfigUtils.isConfigHasChange()) {
            return
        }
        if (isInit.compareAndSet(true, false)) {
            CsContextManager.removeActivityLifecycleCallback(activityLifecycle)
            CsContextManager.removeAppStateChangedCallback(appStateChanged)
        }
    }


    private val activityLifecycle = object : AbstractActivityLifecycle() {
        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            ConfigurationManager.applyConfiguration(activity)
        }
    }

    private val appStateChanged = object : AbstractAppStateChanged() {
        override fun onConfigurationChanged(newConfig: Configuration) {
            ConfigurationManager.systemConfigurationChange(newConfig)
            ConfigurationManager.applyConfiguration(CsContextManager.getApplication())
        }
    }
}
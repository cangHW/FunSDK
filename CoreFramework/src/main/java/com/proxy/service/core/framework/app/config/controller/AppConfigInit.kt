package com.proxy.service.core.framework.app.config.controller

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import com.proxy.service.core.framework.app.config.CsConfigUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.app.context.callback.AbstractAppConfigStateChanged
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
            CsContextManager.addAppConfigStateCallback(appStateChanged)
        }
    }

    fun finish() {
        if (CsConfigUtils.isConfigHasChange()) {
            return
        }
        if (isInit.compareAndSet(true, false)) {
            CsContextManager.removeActivityLifecycleCallback(activityLifecycle)
            CsContextManager.removeAppConfigStateCallback(appStateChanged)
        }
    }


    private val activityLifecycle = object : AbstractActivityLifecycle() {
        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            ConfigurationManager.applyConfiguration(activity)
        }
    }

    private val appStateChanged = object : AbstractAppConfigStateChanged() {
        override fun onConfigurationChanged(newConfig: Configuration) {
            ConfigurationManager.systemConfigurationChange(newConfig)
            ConfigurationManager.applyConfiguration(CsContextManager.getApplication())
        }
    }
}
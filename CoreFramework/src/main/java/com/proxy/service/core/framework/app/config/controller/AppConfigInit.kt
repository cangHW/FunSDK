package com.proxy.service.core.framework.app.config.controller

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import com.proxy.service.core.framework.app.config.CsConfigUtils
import com.proxy.service.core.framework.app.config.controller.impl.ComponentCallbackImpl
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle

/**
 * @author: cangHX
 * @data: 2024/12/24 18:21
 * @desc:
 */
object AppConfigInit {

    fun init() {
        if (!CsConfigUtils.isConfigHasChange()) {
            return
        }
        CsContextManager.addActivityLifecycleCallback(null, true, activityLifecycle)
        CsContextManager.getApplication().registerComponentCallbacks(componentCallback)
    }

    fun finish() {
        if (CsConfigUtils.isConfigHasChange()) {
            return
        }
        CsContextManager.removeActivityLifecycleCallback(activityLifecycle)
        CsContextManager.getApplication().unregisterComponentCallbacks(componentCallback)
    }


    private val activityLifecycle = object : AbstractActivityLifecycle() {
        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            ConfigurationManager.applyConfiguration(activity)
        }
    }

    private val componentCallback = object : ComponentCallbackImpl() {
        override fun onConfigurationChanged(newConfig: Configuration) {
            ConfigurationManager.systemConfigurationChange(newConfig)
            ConfigurationManager.applyConfiguration(CsContextManager.getApplication())
        }
    }
}
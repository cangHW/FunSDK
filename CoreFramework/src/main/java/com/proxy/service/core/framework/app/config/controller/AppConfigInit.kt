package com.proxy.service.core.framework.app.config.controller

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
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
        CsContextManager.addActivityLifecycleCallback(null, true, object : AbstractActivityLifecycle() {
            override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                ConfigurationManager.applyConfiguration(activity)
            }
        })
        val application = CsContextManager.getApplication()
        application.registerComponentCallbacks(object : ComponentCallbackImpl() {
            override fun onConfigurationChanged(newConfig: Configuration) {
                ConfigurationManager.systemConfigurationChange(newConfig)
                ConfigurationManager.applyConfiguration(application)
            }
        })
    }

}
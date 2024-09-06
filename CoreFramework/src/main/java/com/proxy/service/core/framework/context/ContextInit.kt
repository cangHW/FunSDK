package com.proxy.service.core.framework.context

import android.app.Activity
import android.app.Application
import android.content.Context
import com.proxy.service.api.utils.ApiUtils
import com.proxy.service.core.framework.context.lifecycle.ActivityStatusLifecycleImpl
import com.proxy.service.core.framework.context.lifecycle.AppShowStatusLifecycleImpl
import com.proxy.service.core.framework.context.lifecycle.TopActivityLifecycleImpl

/**
 * @author: cangHX
 * @data: 2024/7/2 11:28
 * @desc:
 */
object ContextInit {

    var application: Application? = null

    fun init(context: Context) {
        if (application != null){
            return
        }
        val application: Application = when (context) {
            is Application -> {
                context
            }

            is Activity -> {
                context.application
            }

            else -> {
                val activity = ApiUtils.getActivityFromContext(context) ?: return
                activity.application
            }
        }
        ContextInit.application = application
        ContextInit.application?.registerActivityLifecycleCallbacks(AppShowStatusLifecycleImpl.getInstance())
        ContextInit.application?.registerActivityLifecycleCallbacks(ActivityStatusLifecycleImpl.getInstance())
        ContextInit.application?.registerActivityLifecycleCallbacks(TopActivityLifecycleImpl.getInstance())
    }

}
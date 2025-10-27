package com.proxy.service.core.framework.app.context

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.proxy.service.core.framework.app.context.common.ComponentCallbacksImpl
import com.proxy.service.core.framework.app.context.lifecycle.ActivityLifecycleImpl
import com.proxy.service.core.framework.app.context.lifecycle.AppVisibilityImpl
import com.proxy.service.core.framework.app.context.lifecycle.TopActivityImpl

/**
 * @author: cangHX
 * @data: 2024/7/2 11:28
 * @desc:
 */
object ContextInit {

    @Volatile
    var application: Application? = null

    fun init(context: Context) {
        if (application != null) {
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
                getApplicationFromContext(context) ?: return
            }
        }
        ContextInit.application = application
        ContextInit.application?.registerActivityLifecycleCallbacks(AppVisibilityImpl.getInstance())
        ContextInit.application?.registerActivityLifecycleCallbacks(ActivityLifecycleImpl.getInstance())
        ContextInit.application?.registerActivityLifecycleCallbacks(TopActivityImpl.getInstance())
        ContextInit.application?.registerComponentCallbacks(ComponentCallbacksImpl.getInstance())
    }

    private fun getApplicationFromContext(context: Context): Application? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return ctx.application
            } else if (ctx is Application) {
                return ctx
            }
            ctx = ctx.baseContext
        }
        return null
    }

}
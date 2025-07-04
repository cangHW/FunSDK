package com.proxy.service.core.framework.app.context

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.proxy.service.core.framework.app.context.lifecycle.ActivityStatusLifecycleImpl
import com.proxy.service.core.framework.app.context.lifecycle.AppShowStatusLifecycleImpl
import com.proxy.service.core.framework.app.context.lifecycle.TopActivityLifecycleImpl

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
        ContextInit.application?.registerActivityLifecycleCallbacks(AppShowStatusLifecycleImpl.getInstance())
        ContextInit.application?.registerActivityLifecycleCallbacks(ActivityStatusLifecycleImpl.getInstance())
        ContextInit.application?.registerActivityLifecycleCallbacks(TopActivityLifecycleImpl.getInstance())
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
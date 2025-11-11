package com.proxy.service.core.framework.app.context.lifecycle

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2024/7/2 11:23
 * @desc:
 */
class TopActivityImpl : ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Activity"
        private val mInstance by lazy { TopActivityImpl() }
        fun getInstance(): TopActivityImpl {
            return mInstance
        }
    }

    private val activities = ArrayList<Activity>()

    fun getTopActivity(): Activity? {
        var activity: Activity? = null
        if (activities.size > 0) {
            synchronized(activities) {
                for (i in activities.indices.reversed()) {
                    activity = activities[i]
                    if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                        activity = null
                        continue
                    }
                    break
                }
            }
        }
        if (activity == null) {
            activity = getTopActivityByReflect()
        }
        if (activity?.isFinishing == true || activity?.isDestroyed == true) {
            activity = null
        }
        return activity
    }

    fun getAllActivity(): ArrayList<Activity> {
        return ArrayList(activities)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        addTopActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        addTopActivity(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        addTopActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
    }

    private fun addTopActivity(activity: Activity) {
        synchronized(activities) {
            if (activities.contains(activity)) {
                if (activities.last() == activity) {
                    return
                }
                activities.remove(activity)
            }
            activities.add(activity)
        }
    }

    private fun removeActivity(activity: Activity) {
        synchronized(activities) {
            activities.remove(activity)
        }
    }

    @SuppressLint("PrivateApi")
    private fun getTopActivityByReflect(): Activity? {
        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val currentActivityThreadMethod =
                activityThreadClass.getMethod("currentActivityThread").invoke(null)
            val mActivityListField = activityThreadClass.getDeclaredField("mActivities")
            mActivityListField.isAccessible = true
            val activities = mActivityListField[currentActivityThreadMethod] as Map<*, *>
            for (activityRecord in activities.values) {
                if (activityRecord != null) {
                    val activityRecordClass: Class<*> = activityRecord.javaClass
                    val pausedField = activityRecordClass.getDeclaredField("paused")
                    pausedField.isAccessible = true
                    if (!pausedField.getBoolean(activityRecord)) {
                        val activityField = activityRecordClass.getDeclaredField("activity")
                        activityField.isAccessible = true
                        return activityField[activityRecord] as Activity
                    }
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
        return null
    }

}
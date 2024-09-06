package com.proxy.service.core.framework.context.callback

import android.app.Activity
import android.os.Bundle

/**
 * @author: cangHX
 * @data: 2024/7/2 11:47
 * @desc:
 */
abstract class AbstractActivityLifecycle {

    // *****************  OnCreate  ************************
    open fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    open fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    open fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
    }


    // *****************  OnStart  ************************
    open fun onActivityPreStarted(activity: Activity) {
    }

    open fun onActivityStarted(activity: Activity) {
    }

    open fun onActivityPostStarted(activity: Activity) {
    }


    // *****************  OnResume  ************************
    open fun onActivityPreResumed(activity: Activity) {
    }

    open fun onActivityResumed(activity: Activity) {
    }

    open fun onActivityPostResumed(activity: Activity) {
    }


    // *****************  OnPause  ************************
    open fun onActivityPrePaused(activity: Activity) {
    }

    open fun onActivityPaused(activity: Activity) {
    }

    open fun onActivityPostPaused(activity: Activity) {
    }


    // *****************  OnStop  ************************
    open fun onActivityPreStopped(activity: Activity) {
    }

    open fun onActivityStopped(activity: Activity) {
    }

    open fun onActivityPostStopped(activity: Activity) {
    }


    // *****************  OnSaveInstanceState  ************************
    open fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
    }

    open fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    open fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
    }


    // *****************  OnDestroy  ************************
    open fun onActivityPreDestroyed(activity: Activity) {
    }

    open fun onActivityDestroyed(activity: Activity) {
    }

    open fun onActivityPostDestroyed(activity: Activity) {
    }

}
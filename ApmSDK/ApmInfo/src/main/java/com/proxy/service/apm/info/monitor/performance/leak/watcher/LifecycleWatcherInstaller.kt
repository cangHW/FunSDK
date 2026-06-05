package com.proxy.service.apm.info.monitor.performance.leak.watcher

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractInstaller
import com.proxy.service.apm.info.monitor.performance.leak.composite.AppBackgroundCallback
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.app.context.callback.OnAppVisibilityCallback
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/6/4 16:50
 * @desc:
 */
class LifecycleWatcherInstaller(
    private val objectWatcher: ObjectWatcher,
    private val appBackgroundCallback: AppBackgroundCallback?
) : AbstractInstaller() {

    companion object {
        private const val TAG = "${Constants.TAG}LifecycleWatcherInstaller"
    }

    private val fragmentCallbacks = FragmentLifecycleCallbacks()

    private val appVisibilityCallback = object : OnAppVisibilityCallback {
        override fun onAppBackground() {
            appBackgroundCallback?.whenAppBackground()
        }

        override fun onAppForeground() {
        }
    }

    private val activityLifecycle = object : AbstractActivityLifecycle() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            try {
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                        fragmentCallbacks,
                        true
                    )
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            try {
                objectWatcher.watch(
                    activity,
                    "${activity.javaClass.name} received Activity#onDestroy() callback"
                )
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

    override fun install() {
        objectWatcher.start()
        CsContextManager.addActivityLifecycleCallback(null, lifecycleCallback = activityLifecycle)
        CsContextManager.addAppVisibilityCallback(appVisibilityCallback)
    }

    override fun uninstall() {
        CsContextManager.removeActivityLifecycleCallback(activityLifecycle)
        CsContextManager.removeAppVisibilityCallback(appVisibilityCallback)
        objectWatcher.stop()
    }

    private inner class FragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentDestroyed(fm: FragmentManager, fragment: Fragment) {
            try {
                objectWatcher.watch(
                    fragment,
                    "${fragment.javaClass.name} received Fragment#onDestroy() callback"
                )
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }
}
package com.proxy.service.core.framework.system.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.system.screen.callback.ScreenOrientationCallback
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.framework.system.screen.factory.DisplayController
import com.proxy.service.core.framework.system.screen.info.ScreenInfo


/**
 * 设备屏幕信息工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:32
 * @desc:
 */
object CsScreenUtils {

    /**
     * 获取屏幕宽度，受到实际状态影响
     *
     * @return 返回屏幕宽度
     */
    fun getScreenWidth(): Int {
        val metric = CsContextManager.getApplication().resources.displayMetrics
        return metric.widthPixels
    }

    /**
     * 获取屏幕高度，受到实际状态影响
     *
     * @return 返回屏幕高度
     */
    fun getScreenHeight(): Int {
        val metric = CsContextManager.getApplication().resources.displayMetrics
        return metric.heightPixels
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val res = CsContextManager.getApplication().resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * 获取导航栏高度
     *
     * @return 导航栏高度
     */
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getNavigationBarHeight(): Int {
        val resources = CsContextManager.getApplication().resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    /**
     * 获取标题栏高度
     *
     * @return 标题栏高度
     */
    fun getActionBarHeight(): Int {
        val context = CsContextManager.getApplication()
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            TypedValue.complexToDimensionPixelSize(
                typedValue.data,
                context.resources.displayMetrics
            )
        } else {
            0
        }
    }

    /**
     * 获取屏幕真实尺寸等信息，不受状态栏、导航栏等影响
     * */
    fun getScreenRealInfo(): ScreenInfo {
        val info = ScreenInfo()

        val res = CsContextManager.getApplication().resources

        info.isPortrait = res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        info.dpi = res.displayMetrics.densityDpi
        info.statusBarHeight = getStatusBarHeight()
        info.navigationBarHeight = getNavigationBarHeight()
        info.actionBarHeight = getActionBarHeight()

        val service = CsContextManager.getApplication().getSystemService(Context.WINDOW_SERVICE)
            ?: return info
        val windowManager = service as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            info.screenWidth = windowManager.maximumWindowMetrics.bounds.width()
            info.screenHeight = windowManager.maximumWindowMetrics.bounds.height()
        } else {
            val display = windowManager.defaultDisplay
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val size = Point()
                display.getRealSize(size)
                info.screenWidth = size.x
                info.screenHeight = size.y
            } else {
                val metrics = DisplayMetrics()
                display.getMetrics(metrics)
                info.screenWidth = metrics.widthPixels
                info.screenHeight = metrics.heightPixels
            }
        }

        return info
    }

    /**
     * 是否是竖屏
     * */
    fun isPortrait(): Boolean {
        val res = CsContextManager.getApplication().resources
        return res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 是否是横屏
     * */
    fun isLandscape(): Boolean {
        val res = CsContextManager.getApplication().resources
        return res.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 获取屏幕旋转角度
     * */
    fun getScreenRotation(activity: Activity? = null): RotationEnum {
        val context = CsContextManager.getApplication()

        val degree = if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display?.rotation
        } else {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager?
            wm?.defaultDisplay?.rotation
        }

        return RotationEnum.valueOf(degree) ?: RotationEnum.ROTATION_0
    }

    /**
     * 添加屏幕旋转监听, 需要配置旋转屏幕时 activity 不销毁
     *
     * <manifest
     * .
     * .
     *  <application
     *
     *      <activity
     *          android:name=".xx.xx.XXActivity"
     *          android:configChanges="orientation|screenSize" />
     *  </application>
     * </manifest>
     * */
    fun addScreenRotationCallback(callback: ScreenRotationCallback) {
        DisplayController.instance.addCallback(callback)
    }

    /**
     * 移除屏幕旋转监听
     * */
    fun removeScreenRotationCallback(callback: ScreenRotationCallback) {
        DisplayController.instance.removeCallback(callback)
    }

    /**
     * 添加横竖屏变化监听
     *
     * <manifest
     * .
     * .
     *  <application
     *
     *      <activity
     *          android:name=".xx.xx.XXActivity"
     *          android:configChanges="orientation|screenSize" />
     *  </application>
     * </manifest>
     * */
    fun addScreenOrientationCallback(callback: ScreenOrientationCallback) {
        DisplayController.instance.addCallback(callback)
    }

    /**
     * 移除横竖屏变化监听
     * */
    fun removeScreenOrientationCallback(callback: ScreenOrientationCallback) {
        DisplayController.instance.removeCallback(callback)
    }

}
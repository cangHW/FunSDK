package com.proxy.service.core.framework.system.screen

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.resource.CsResUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenOrientationCallback
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.callback.SensorRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.core.framework.system.screen.factory.OrientationFactory
import com.proxy.service.core.framework.system.screen.factory.RotationFactory
import com.proxy.service.core.framework.system.screen.factory.SensorFactory
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
    fun getStatusBarHeight(): Int {
        return CsResUtils.getDimenPxByName("status_bar_height", "android")
    }

    /**
     * 获取导航栏高度
     *
     * @return 导航栏高度
     */
    fun getNavigationBarHeight(): Int {
        return CsResUtils.getDimenPxByName("navigation_bar_height", "android")
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

        val service = CsContextManager.getApplication()
            .getSystemService(Context.WINDOW_SERVICE)
            ?: return info
        val windowManager = service as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            info.screenWidth = windowManager.maximumWindowMetrics.bounds.width()
            info.screenHeight = windowManager.maximumWindowMetrics.bounds.height()
        } else {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getRealSize(size)
            info.screenWidth = size.x
            info.screenHeight = size.y
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
        return RotationFactory.instance.getRotationEnum(activity) ?: RotationEnum.ROTATION_0
    }

    /**
     * 添加屏幕旋转角度监听, 弱引用.
     * 尽量保证在旋转屏幕时如果当前对象会被销毁, 则在销毁前移除监听, 避免内存泄漏, 或者配置旋转屏幕时当前对象不销毁。
     *
     * Activity 配置方式
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
    fun addWeakScreenRotationCallback(
        callback: ScreenRotationCallback,
        activity: Activity? = null
    ) {
        RotationFactory.instance.addWeakCallback(activity, callback)
    }

    /**
     * 移除屏幕旋转监听
     * */
    fun removeScreenRotationCallback(callback: ScreenRotationCallback) {
        RotationFactory.instance.removeCallback(callback)
    }

    /**
     * 添加传感器检测旋转角度监听, 弱引用.
     * 尽量保证在旋转时如果当前对象会被销毁, 则在销毁前移除监听, 避免内存泄漏, 或者配置旋转时当前对象不销毁。
     *
     * Activity 配置方式
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
    fun addWeakSensorRotationCallback(callback: SensorRotationCallback) {
        SensorFactory.instance.addWeakCallback(callback)
    }

    /**
     * 移除传感器检测旋转监听
     * */
    fun removeSensorRotationCallback(callback: SensorRotationCallback) {
        SensorFactory.instance.removeCallback(callback)
    }

    /**
     * 添加横竖屏旋转监听, 弱引用.
     * 尽量保证在横竖屏旋转时如果当前对象会被销毁, 则在销毁前移除监听, 避免内存泄漏, 或者配置横竖屏旋转时当前对象不销毁。
     *
     * Activity 配置方式
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
    fun addWeakScreenOrientationCallback(callback: ScreenOrientationCallback) {
        OrientationFactory.instance.addWeakCallback(callback)
    }

    /**
     * 移除横竖屏变化监听
     * */
    fun removeScreenOrientationCallback(callback: ScreenOrientationCallback) {
        OrientationFactory.instance.removeCallback(callback)
    }

}
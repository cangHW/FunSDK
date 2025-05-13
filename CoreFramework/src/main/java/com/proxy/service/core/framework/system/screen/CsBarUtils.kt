package com.proxy.service.core.framework.system.screen

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.device.DeviceType
import com.proxy.service.core.framework.system.device.CsDeviceUtils

/**
 * 导航栏、状态栏工具
 *
 * @author: cangHX
 * @data: 2024/6/4 10:28
 * @desc:
 */
object CsBarUtils {

    private const val TAG = "${CoreConfig.TAG}Bar"

    internal interface IBarStatus {

        /**
         * 设置状态栏模式
         * */
        fun setStatusBarModel(window: Window, isDarkModel: Boolean)

        /**
         * 设置状态栏透明
         * */
        fun setStatusBarTransparent(window: Window)

        /**
         * 设置导航栏透明
         * */
        fun setNavigationBarTransparent(window: Window)
    }

    private var impl: IBarStatus? = null

    init {
        impl = if (CsDeviceUtils.getDeviceType() == DeviceType.Xiaomi) {
            MIUIStatusBarImpl()
        } else if (CsDeviceUtils.getDeviceType() == DeviceType.MeiZu) {
            MeizuStatusBarImpl()
        } else {
            NormalStatusBarImpl()
        }
    }

    /**
     * 设置透明状态栏
     * @param isDarkModel   是否是黑夜模式, 黑夜模式下状态栏字体颜色为白色
     * */
    fun setStatusBarTransparent(activity: Activity, isDarkModel: Boolean = false) {
        impl?.setStatusBarTransparent(activity.window)
        impl?.setStatusBarModel(activity.window, isDarkModel)
    }

    /**
     * 设置透明状态栏
     * @param isDarkModel   是否是黑夜模式, 黑夜模式下状态栏字体颜色为白色
     * */
    fun setStatusBarTransparent(window: Window, isDarkModel: Boolean = false) {
        impl?.setStatusBarTransparent(window)
        impl?.setStatusBarModel(window, isDarkModel)
    }

    /**
     * 设置透明导航栏
     * */
    fun setNavigationBarTransparent(activity: Activity) {
        impl?.setNavigationBarTransparent(activity.window)
    }

    /**
     * 设置透明导航栏
     * */
    fun setNavigationBarTransparent(window: Window) {
        impl?.setNavigationBarTransparent(window)
    }


    /**
     * 小米
     * */
    private class MIUIStatusBarImpl : IBarStatus {
        override fun setStatusBarModel(window: Window, isDarkModel: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NormalStatusBarImpl().setStatusBarModel(window, isDarkModel)
            } else {
                val clazz: Class<out Window?> = window.javaClass
                try {
                    val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                    val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                    val darkModeFlag = field.getInt(layoutParams)
                    val extraFlagField = clazz.getMethod(
                        "setExtraFlags",
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                    extraFlagField.invoke(
                        window,
                        if (isDarkModel) darkModeFlag else 0,
                        darkModeFlag
                    )
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
            }
        }

        override fun setStatusBarTransparent(window: Window) {
            NormalStatusBarImpl().setStatusBarTransparent(window)
        }

        override fun setNavigationBarTransparent(window: Window) {
            NormalStatusBarImpl().setNavigationBarTransparent(window)
        }
    }

    /**
     * 魅族
     * */
    private class MeizuStatusBarImpl : IBarStatus {
        override fun setStatusBarModel(window: Window, isDarkModel: Boolean) {
            val params = window.attributes
            try {
                val darkFlag =
                    WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags =
                    WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(params)
                value = if (isDarkModel) {
                    value and bit.inv()
                } else {
                    value or bit
                }
                meizuFlags.setInt(params, value)
                window.attributes = params
                darkFlag.isAccessible = false
                meizuFlags.isAccessible = false
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }

        override fun setStatusBarTransparent(window: Window) {
            NormalStatusBarImpl().setStatusBarTransparent(window)
        }

        override fun setNavigationBarTransparent(window: Window) {
            NormalStatusBarImpl().setNavigationBarTransparent(window)
        }
    }

    /**
     * 默认
     * */
    private class NormalStatusBarImpl : IBarStatus {

        override fun setStatusBarModel(window: Window, isDarkModel: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.let {
                    if (isDarkModel) {
                        it.setSystemBarsAppearance(
                            0,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    } else {
                        it.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decorView = window.decorView
                val systemUiVisibility = decorView.systemUiVisibility
                if (isDarkModel) {
                    decorView.systemUiVisibility =
                        systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                } else {
                    decorView.systemUiVisibility =
                        systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }

        override fun setStatusBarTransparent(window: Window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                val option =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                val vis = window.decorView.systemUiVisibility
                window.decorView.systemUiVisibility = option or vis
                window.statusBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        override fun setNavigationBarTransparent(window: Window) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                return
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.navigationBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION == 0) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                }
            }
            val decorView = window.decorView
            val vis = decorView.systemUiVisibility
            val option =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = vis or option
        }
    }
}
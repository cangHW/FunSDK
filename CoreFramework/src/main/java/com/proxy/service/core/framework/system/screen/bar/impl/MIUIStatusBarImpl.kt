package com.proxy.service.core.framework.system.screen.bar.impl

import android.os.Build
import android.view.Window
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/1/15 21:48
 * @desc: 小米
 */
class MIUIStatusBarImpl: DefaultBarStatus() {

    override fun setStatusBarModel(window: Window, isDarkModel: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.setStatusBarModel(window, isDarkModel)
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
}
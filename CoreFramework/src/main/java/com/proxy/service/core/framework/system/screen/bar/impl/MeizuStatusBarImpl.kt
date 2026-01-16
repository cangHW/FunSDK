package com.proxy.service.core.framework.system.screen.bar.impl

import android.view.Window
import android.view.WindowManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/1/15 21:58
 * @desc: 魅族
 */
class MeizuStatusBarImpl : DefaultBarStatus() {

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

}
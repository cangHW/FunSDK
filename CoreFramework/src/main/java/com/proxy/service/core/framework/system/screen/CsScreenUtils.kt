package com.proxy.service.core.framework.system.screen

import android.annotation.SuppressLint
import android.util.TypedValue
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2024/4/28 17:32
 * @desc:
 */
object CsScreenUtils {

    /**
     * 获取屏幕宽度
     *
     * @return 返回屏幕宽度
     */
    fun getScreenWidth(): Int {
        val metric = CsContextManager.getApplication().resources.displayMetrics
        return metric.widthPixels
    }

    /**
     * 获取屏幕高度
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
        if (statusBarHeight == 0) {
            statusBarHeight = CsDpUtils.dp2px(25f)
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
            TypedValue.complexToDimensionPixelSize(typedValue.data, context.resources.displayMetrics)
        } else {
            0
        }
    }

}
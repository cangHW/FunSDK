package com.proxy.service.core.framework.app.resource

import android.util.TypedValue
import com.proxy.service.core.framework.app.context.CsContextManager
import kotlin.math.roundToInt

/**
 * @author: cangHX
 * @data: 2024/4/28 17:33
 * @desc:
 */
object CsDpUtils {

    /**
     * dp 转 px
     * */
    fun dp2px(dpValue: Float): Int {
        val dm = CsContextManager.getApplication().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm).roundToInt()
    }

    /**
     * px 转 dp
     * */
    fun px2dp(pxValue: Float): Int {
        val density = CsContextManager.getApplication().resources.displayMetrics.density
        return (pxValue / density).roundToInt()
    }

    /**
     * sp 转 px
     * */
    fun sp2px(spValue: Float): Int {
        val dm = CsContextManager.getApplication().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm).roundToInt()
    }

    /**
     * px 转 sp
     * */
    fun px2sp(pxValue: Float): Int {
        val scaledDensity = CsContextManager.getApplication().resources.displayMetrics.scaledDensity
        return (pxValue / scaledDensity).roundToInt()
    }

}
package com.proxy.service.core.framework.app.resource

import android.util.TypedValue
import androidx.annotation.DimenRes
import com.proxy.service.core.framework.app.context.CsContextManager
import kotlin.math.roundToInt

/**
 * 资源，dp、sp、px 转换相关工具
 *
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
     * dp 转 px
     * */
    fun dp2pxf(dpValue: Float): Float {
        val dm = CsContextManager.getApplication().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm)
    }

    /**
     * px 转 dp
     * */
    fun px2dp(pxValue: Float): Int {
        val density = CsContextManager.getApplication().resources.displayMetrics.density
        return (pxValue / density).roundToInt()
    }

    /**
     * px 转 dp
     * */
    fun px2dpf(pxValue: Float): Float {
        val density = CsContextManager.getApplication().resources.displayMetrics.density
        return pxValue / density
    }

    /**
     * sp 转 px
     * */
    fun sp2px(spValue: Float): Int {
        val dm = CsContextManager.getApplication().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm).roundToInt()
    }

    /**
     * sp 转 px
     * */
    fun sp2pxf(spValue: Float): Float {
        val dm = CsContextManager.getApplication().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm)
    }

    /**
     * px 转 sp
     * */
    fun px2sp(pxValue: Float): Int {
        val scaledDensity = CsContextManager.getApplication().resources.displayMetrics.scaledDensity
        return (pxValue / scaledDensity).roundToInt()
    }

    /**
     * px 转 sp
     * */
    fun px2spf(pxValue: Float): Float {
        val scaledDensity = CsContextManager.getApplication().resources.displayMetrics.scaledDensity
        return pxValue / scaledDensity
    }


    /**
     * dimension 值转 px
     * */
    fun res2px(@DimenRes resId: Int): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getDimensionPixelSize(resId)
    }

    /**
     * dimension 值转 px
     * */
    fun res2pxf(@DimenRes resId: Int): Float {
        val resources = CsContextManager.getApplication().resources
        return resources.getDimension(resId)
    }
}
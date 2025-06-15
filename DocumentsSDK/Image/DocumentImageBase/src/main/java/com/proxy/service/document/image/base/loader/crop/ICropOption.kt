package com.proxy.service.document.image.base.loader.crop

import android.graphics.RectF
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback
import com.proxy.service.document.image.base.loader.base.ILoader

/**
 * @author: cangHX
 * @data: 2025/6/3 09:58
 * @desc:
 */
interface ICropOption : ILoader<ICropController> {

    /**
     * 设置裁剪区域宽高, 视图居中区域
     * */
    fun setCropSize(widthPx: Float, heightPx: Float): ICropOption

    /**
     * 设置裁剪区域位置
     * */
    fun setCropRect(rect: RectF): ICropOption

    /**
     * 设置遮罩颜色, 默认颜色为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_MASK_COLOR]
     * */
    fun setMaskColor(@ColorInt color: Int): ICropOption

    /**
     * 设置遮罩颜色, 默认颜色为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_MASK_COLOR]
     * */
    fun setMaskColorString(colorString: String): ICropOption

    /**
     * 设置遮罩颜色, 默认颜色为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_MASK_COLOR]
     * */
    fun setMaskColorRes(@ColorRes id: Int): ICropOption

    /**
     * 设置裁剪框颜色, 默认颜色为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_LINE_COLOR]
     * */
    fun setCropLineColor(@ColorInt color: Int): ICropOption

    /**
     * 设置裁剪框颜色, 默认颜色为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_LINE_COLOR]
     * */
    fun setCropLineColorString(colorString: String): ICropOption

    /**
     * 设置裁剪框颜色, 默认颜色为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_LINE_COLOR]
     * */
    fun setCropLineColorRes(@ColorRes id: Int): ICropOption

    /**
     * 设置裁剪框线宽, 默认宽度为 [com.proxy.service.document.base.constants.Constants.DEFAULT_CROP_LINE_WIDTH]
     * */
    fun setCropLineWidth(width: Float): ICropOption

    /**
     * 设置裁剪框绘制回调
     * */
    fun setDrawCropCallback(callback: OnDrawCropCallback): ICropOption
}
package com.proxy.service.document.image.base.loader.crop

import android.graphics.RectF
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.loader.base.ILoader
import com.proxy.service.document.image.base.mode.CropMode

/**
 * @author: cangHX
 * @data: 2025/6/3 09:58
 * @desc:
 */
interface ICropOption : ILoader<ICropController> {

    /**
     * 设置裁剪区域与 bitmap 相同
     * */
    fun setCropFrameRectToFitBitmap(): ICropOption

    /**
     * 设置裁剪区域宽高, 视图居中区域, 注意预留出裁剪框的绘制区域.
     *
     * 举例: 裁剪区域为[100, 100, 200, 200], 裁剪框线宽为 8.
     * 则裁剪框绘制位置为:
     * 1、上边[92, 92, 208, 100]
     * 2、右边[200, 92, 208, 208]
     * 3、下边[92, 200, 208, 208]
     * 4、左边[92, 92, 100, 208]
     * */
    fun setCropFrameSize(widthPx: Float, heightPx: Float): ICropOption

    /**
     * 设置裁剪区域位置, 注意预留出裁剪框的绘制区域.
     *
     * 举例: 裁剪区域为[100, 100, 200, 200], 裁剪框线宽为 8.
     * 则裁剪框绘制位置为:
     * 1、上边[92, 92, 208, 100]
     * 2、右边[200, 92, 208, 208]
     * 3、下边[92, 200, 208, 208]
     * 4、左边[92, 92, 100, 208]
     * */
    fun setCropFrameRect(rect: RectF): ICropOption

    /**
     * 设置遮罩颜色, 默认颜色为 [ImageConstants.DEFAULT_CROP_MASK_COLOR]
     * */
    fun setMaskColor(@ColorInt color: Int): ICropOption

    /**
     * 设置遮罩颜色, 默认颜色为 [ImageConstants.DEFAULT_CROP_MASK_COLOR]
     * */
    fun setMaskColorString(colorString: String): ICropOption

    /**
     * 设置遮罩颜色, 默认颜色为 [ImageConstants.DEFAULT_CROP_MASK_COLOR]
     * */
    fun setMaskColorRes(@ColorRes id: Int): ICropOption

    /**
     * 设置裁剪框颜色, 默认颜色为 [ImageConstants.DEFAULT_CROP_FRAME_LINE_COLOR]
     * */
    fun setCropFrameLineColor(@ColorInt color: Int): ICropOption

    /**
     * 设置裁剪框颜色, 默认颜色为 [ImageConstants.DEFAULT_CROP_FRAME_LINE_COLOR]
     * */
    fun setCropFrameLineColorString(colorString: String): ICropOption

    /**
     * 设置裁剪框颜色, 默认颜色为 [ImageConstants.DEFAULT_CROP_FRAME_LINE_COLOR]
     * */
    fun setCropFrameLineColorRes(@ColorRes id: Int): ICropOption

    /**
     * 设置裁剪框线宽, 默认宽度为 [ImageConstants.DEFAULT_CROP_FRAME_LINE_WIDTH]
     * */
    fun setCropFrameLineWidth(width: Float): ICropOption

    /**
     * 设置裁剪模式, 默认为: BaseImageMoveAndScaleMode
     * */
    fun setCropMode(mode: CropMode): ICropOption

    /**
     * 设置裁剪框绘制回调
     * */
    fun setDrawCropCallback(callback: OnDrawCropCallback): ICropOption
}
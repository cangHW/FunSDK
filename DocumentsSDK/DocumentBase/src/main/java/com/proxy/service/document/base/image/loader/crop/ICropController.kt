package com.proxy.service.document.base.image.loader.crop

import com.proxy.service.document.base.image.callback.crop.OnCropCallback

/**
 * @author: cangHX
 * @data: 2025/6/3 09:58
 * @desc:
 */
interface ICropController {

    /**
     * 开始裁剪
     * */
    fun startCrop(callback: OnCropCallback)

    /**
     * 开始裁剪并设置最终 bitmap 的宽高，可能会被拉伸
     *
     * @param width     最终 bitmap 的宽度
     * @param height    最终 bitmap 的高度
     * */
    fun startCrop(width: Int, height: Int, callback: OnCropCallback)

    /**
     * 开始裁剪并设置最终 bitmap 的宽高
     *
     * @param width             最终 bitmap 的宽度
     * @param height            最终 bitmap 的高度
     * @param keepAspectRatio   是否保持宽高比例
     * */
    fun startCrop(width: Int, height: Int, keepAspectRatio: Boolean, callback: OnCropCallback)

}
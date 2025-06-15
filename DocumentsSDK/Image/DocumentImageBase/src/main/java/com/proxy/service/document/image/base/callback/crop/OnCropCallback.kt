package com.proxy.service.document.image.base.callback.crop

import android.graphics.Bitmap

/**
 * @author: cangHX
 * @data: 2025/6/4 09:39
 * @desc:
 */
interface OnCropCallback {

    companion object {
        /**
         * 裁剪成功
         * */
        const val CROP_STATUS_OK: Int = 0

        /**
         * 加载失败
         * */
        const val CROP_STATUS_LOAD_ERROR: Int = -1

        /**
         * 裁剪失败
         * */
        const val CROP_STATUS_CROP_ERROR: Int = -2
    }

    /**
     * 裁剪结果回调
     *
     * @param status    裁剪的状态, [OnCropCallback.CROP_STATUS_OK] 等
     * @param bitmap    裁剪后的图片
     * */
    fun onCropResult(status: Int, bitmap: Bitmap?)

}
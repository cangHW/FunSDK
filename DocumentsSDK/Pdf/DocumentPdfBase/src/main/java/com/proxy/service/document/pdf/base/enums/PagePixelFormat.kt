package com.proxy.service.document.pdf.base.enums

import android.graphics.Bitmap

/**
 * @author: cangHX
 * @data: 2025/6/25 15:17
 * @desc:
 */
enum class PagePixelFormat(val value: Bitmap.Config) {

    /**
     * 低清晰度, 占用内存更少
     * */
    RGB_565(Bitmap.Config.RGB_565),

    /**
     * 高清晰度, 视觉效果更好
     * */
    ARGB_8888(Bitmap.Config.ARGB_8888);

}
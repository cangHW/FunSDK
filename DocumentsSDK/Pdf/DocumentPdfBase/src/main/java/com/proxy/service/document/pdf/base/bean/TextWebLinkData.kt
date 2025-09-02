package com.proxy.service.document.pdf.base.bean

import android.graphics.RectF

/**
 * @author: cangHX
 * @data: 2025/4/30 16:10
 * @desc: 文字中的链接
 */
class TextWebLinkData(
    /**
     * 链接指向的外部资源的地址
     * */
    val uri: String,
    /**
     * 链接的矩形区域
     * */
    val bounds: ArrayList<RectF>
) {

    override fun toString(): String {
        return "TextWebLinkData(uri='$uri, bounds=$bounds')"
    }
}
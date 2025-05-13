package com.proxy.service.document.base.pdf.info

import android.graphics.RectF

/**
 * @author: cangHX
 * @data: 2025/4/30 16:10
 * @desc:
 */
class LinkData(
    /**
     * 链接的矩形区域
     * */
    val bounds: RectF,
    /**
     * 链接跳转的目标页码, 可能不存在
     * */
    val destPageIdx: Int?,
    /**
     * 链接指向的外部资源的地址, 可能不存在
     * */
    val uri: String?
) {

    override fun toString(): String {
        return "LinkData(bounds=$bounds, destPageIdx=$destPageIdx, uri='$uri')"
    }
}
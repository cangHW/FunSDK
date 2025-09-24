package com.proxy.service.widget.info.toast.enums

import android.view.Gravity

/**
 * @author: cangHX
 * @data: 2025/7/8 18:37
 * @desc:
 */
enum class ToastGravity(val value: Int) {

    /**
     * 屏幕左侧
     * */
    LEFT(Gravity.LEFT),

    /**
     * 屏幕右侧
     * */
    RIGHT(Gravity.RIGHT),

    /**
     * 屏幕顶部
     * */
    TOP(Gravity.TOP),

    /**
     * 屏幕中间
     * */
    CENTER(Gravity.CENTER),

    /**
     * 屏幕底部
     * */
    BOTTOM(Gravity.BOTTOM);

}
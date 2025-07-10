package com.proxy.service.widget.info.toast

/**
 * @author: cangHX
 * @data: 2025/7/8 18:35
 * @desc:
 */
class ToastConfig {

    /**
     * 距离屏幕左右间距百分比（0-1）
     * */
    var horizontalMargin: Float = 0f

    /**
     * 距离屏幕上下间距百分比（0-1）
     * */
    var verticalMargin: Float = 0.1f


    /**
     * 位于屏幕位置
     * */
    var gravity: ToastGravity = ToastGravity.BOTTOM

    /**
     * 距离屏幕原点偏移值
     * */
    var xOffsetPx: Int = 0

    /**
     * 距离屏幕原点偏移值
     * */
    var yOffsetPx: Int = 0

}
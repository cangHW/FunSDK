package com.proxy.service.document.image.base.callback.base

import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2025/6/6 09:48
 * @desc:
 */
interface OnDoubleClickCallback {

    /**
     * 双击事件回调
     *
     * @param event 事件信息
     * */
    fun onDoubleClick(event: MotionEvent)

}
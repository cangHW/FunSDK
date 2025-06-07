package com.proxy.service.document.base.image.callback.base

import android.view.MotionEvent

/**
 * @author: cangHX
 * @data: 2025/6/7 10:56
 * @desc:
 */
interface OnTouchEventCallback {

    /**
     * 触摸事件回调
     *
     * @param event 事件信息
     *
     * @return 为 true 表示已经自行处理这次变化, false 仍然会执行原定逻辑
     * */
    fun onTouchEvent(event: MotionEvent): Boolean

}
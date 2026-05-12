package com.proxy.service.document.image.base.callback.base

import android.view.MotionEvent
import com.proxy.service.document.image.base.loader.base.IController

/**
 * @author: cangHX
 * @date: 2025/6/6 09:48
 * @desc:
 */
interface OnLongPressCallback {

    /**
     * 长按事件回调
     *
     * @param event 事件信息
     * */
    fun onLongPress(controller: IController, event: MotionEvent)

}
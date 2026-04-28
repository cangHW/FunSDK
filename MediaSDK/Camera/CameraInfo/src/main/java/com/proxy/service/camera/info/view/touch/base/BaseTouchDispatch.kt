package com.proxy.service.camera.info.view.touch.base

import android.view.View
import com.proxy.service.camera.base.callback.view.CustomTouchDispatch

/**
 * @author: cangHX
 * @data: 2026/2/11 10:14
 * @desc:
 */
abstract class BaseTouchDispatch(
    private val view: View
) : CustomTouchDispatch() {

    protected fun postInvalidate() {
        view.postInvalidate()
    }

    protected fun post(runnable: Runnable) {
        view.post(runnable)
    }

}
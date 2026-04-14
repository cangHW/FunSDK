package com.proxy.service.camera.info.page.manager

import android.view.MotionEvent
import com.proxy.service.camera.base.callback.view.CustomTouchDispatch
import com.proxy.service.core.framework.app.resource.CsDpUtils
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

/**
 * @author: cangHX
 * @data: 2026/4/9 17:32
 * @desc:
 */
class CameraCustomTouchDispatch private constructor(
    private val callback: CameraCustomTouchCallback
) : CustomTouchDispatch() {

    interface CameraCustomTouchCallback {
        fun moveLeft()
        fun moveRight()
    }

    companion object {
        fun create(callback: CameraCustomTouchCallback): CameraCustomTouchDispatch {
            return CameraCustomTouchDispatch(callback)
        }
    }

    private val minScrollSlop = CsDpUtils.dp2pxf(10f)

    private val isDown = AtomicBoolean(false)
    private var moveX: Float = 0f
    private var moveY: Float = 0f


    private fun reset() {
        moveX = 0f
        moveY = 0f

        isDown.set(false)
    }

    override fun onViewDown(e: MotionEvent) {
        super.onViewDown(e)

        reset()
        isDown.set(true)
    }

    override fun onViewScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
        if (!isDown.get()) {
            return
        }

        if (e2.pointerCount > 1) {
            reset()
            return
        }

        moveX += distanceX
        moveY += distanceY

        val absX = abs(moveX)

        if (abs(moveY) > absX) {
            reset()
            return
        }

        if (absX > minScrollSlop) {
            if (isDown.compareAndSet(true, false)) {
                if (moveX > 0) {
                    callback.moveLeft()
                } else {
                    callback.moveRight()
                }
            }
        }
    }

}
package com.proxy.service.camera.info.view.touch.mode

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import com.proxy.service.camera.info.view.touch.base.BaseTouchDispatch
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/2/11 10:11
 * @desc:
 */
class MeteringModeDispatch(
    private val view: View,
    private val handler: IHandlerOption?
) : BaseTouchDispatch(view) {

    data class MeteringTouchResult(
        val rect: RectF?,
        val needReset: Boolean = true
    )

    interface OnCameraMeteringIntercept {
        /**
         * 检测是否触发触摸测光
         * */
        fun onTouchMeteringIntercept(
            event: MotionEvent,
            viewWidth: Int,
            viewHeight: Int
        ): MeteringTouchResult?

        /**
         * 触摸测光超时，恢复自动测光
         * */
        fun onTouchMeteringReset()
    }

    companion object {
        private const val TASK_METERING = "task_metering"
        private const val TASK_METERING_DELAY_SECONDS = 3L
        private val TASK_METERING_LINE_WIDTH = CsDpUtils.dp2pxf(1f)

    }

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var meteringRect: RectF? = null

    private var meteringModeIntercept: OnCameraMeteringIntercept? = null

    fun setOnCameraMeteringIntercept(intercept: OnCameraMeteringIntercept) {
        this.meteringModeIntercept = intercept
    }

    override fun onViewSingleTap(e: MotionEvent) {
        super.onViewSingleTap(e)

        val result = meteringModeIntercept?.onTouchMeteringIntercept(e, view.width, view.height)
        meteringRect = result?.rect
        meteringRect?.let {
            postInvalidate()
        }

        if (result?.needReset == true) {
            handler?.clearAllTaskWithTag(TASK_METERING)
            handler?.setDelay(TASK_METERING_DELAY_SECONDS, TimeUnit.SECONDS)?.start(TASK_METERING) {
                meteringRect = null
                postInvalidate()
                meteringModeIntercept?.onTouchMeteringReset()
            }
        }
    }

    override fun onViewDraw(canvas: Canvas) {
        val rect = meteringRect ?: return

        val offsetW = rect.width() / 3
        val offsetH = rect.height() / 3

        paint.color = Color.WHITE
        paint.strokeWidth = TASK_METERING_LINE_WIDTH

        canvas.drawLine(rect.left, rect.top, rect.left + offsetW, rect.top, paint)
        canvas.drawLine(rect.right - offsetW, rect.top, rect.right, rect.top, paint)

        canvas.drawLine(rect.left, rect.bottom, rect.left + offsetW, rect.bottom, paint)
        canvas.drawLine(rect.right - offsetW, rect.bottom, rect.right, rect.bottom, paint)

        canvas.drawLine(rect.left, rect.top, rect.left, rect.top + offsetH, paint)
        canvas.drawLine(rect.left, rect.bottom - offsetH, rect.left, rect.bottom, paint)

        canvas.drawLine(rect.right, rect.top, rect.right, rect.top + offsetH, paint)
        canvas.drawLine(rect.right, rect.bottom - offsetH, rect.right, rect.bottom, paint)
    }

}
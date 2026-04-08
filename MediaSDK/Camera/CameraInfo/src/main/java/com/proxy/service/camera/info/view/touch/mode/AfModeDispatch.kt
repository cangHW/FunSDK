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
class AfModeDispatch(
    view: View,
    private val handler: IHandlerOption?
) : BaseTouchDispatch(view) {

    interface OnCameraAfIntercept {
        /**
         * 检测是否触发手动对焦
         * */
        fun onTouchAfIntercept(event: MotionEvent): RectF?
    }

    companion object {
        private const val TASK_AF = "task_af"
        private const val TASK_AF_DELAY_SECONDS = 5L
        private val TASK_AF_LINE_WIDTH = CsDpUtils.dp2pxf(1f)

    }

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var afRect: RectF? = null

    private var cameraAfIntercept: OnCameraAfIntercept? = null

    fun setOnCameraAfIntercept(intercept: OnCameraAfIntercept) {
        this.cameraAfIntercept = intercept
    }

    override fun onViewSingleTap(e: MotionEvent) {
        super.onViewSingleTap(e)

        afRect = cameraAfIntercept?.onTouchAfIntercept(e)
        afRect?.let {
            handler?.clearAllTaskWithTag(TASK_AF)
            postInvalidate()
            handler?.setDelay(TASK_AF_DELAY_SECONDS, TimeUnit.SECONDS)?.start(TASK_AF) {
                afRect = null
                postInvalidate()
            }
        }
    }

    override fun onViewDraw(canvas: Canvas) {
        val rect = afRect ?: return

        val offsetW = rect.width() / 3
        val offsetH = rect.height() / 3

        paint.color = Color.WHITE
        paint.strokeWidth = TASK_AF_LINE_WIDTH

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
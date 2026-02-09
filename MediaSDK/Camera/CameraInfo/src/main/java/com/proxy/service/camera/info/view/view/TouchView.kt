package com.proxy.service.camera.info.view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.core.service.task.CsTask
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/2/8 15:59
 * @desc:
 */
class TouchView : View {

    interface OnCameraTouchAfIntercept {
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

    private val handler = CsTask.launchTaskGroup("Media-Camera-TouchView")
    private var cameraTouchAfIntercept: OnCameraTouchAfIntercept? = null

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var afRect: RectF? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setOnCameraTouchAfIntercept(intercept: OnCameraTouchAfIntercept) {
        this.cameraTouchAfIntercept = intercept
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                afRect = cameraTouchAfIntercept?.onTouchAfIntercept(event)
                afRect?.let {
                    handler?.clearAllTaskWithTag(TASK_AF)
                    postInvalidate()
                    handler?.setDelay(TASK_AF_DELAY_SECONDS, TimeUnit.SECONDS)?.start(TASK_AF) {
                        afRect = null
                        postInvalidate()
                    }
                }
            }
        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
        drawAf(canvas)
    }

    private fun drawAf(canvas: Canvas) {
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
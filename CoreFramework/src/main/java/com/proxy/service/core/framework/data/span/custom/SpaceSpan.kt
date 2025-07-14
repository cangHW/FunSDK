package com.proxy.service.core.framework.data.span.custom

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

/**
 * 空格 span
 *
 * @author: cangHX
 * @data: 2025/7/14 10:47
 * @desc:
 */
class SpaceSpan(
    private val width: Int,
    color: Int
) : ReplacementSpan() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.style = Paint.Style.FILL
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return width
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), this.paint)
    }
}
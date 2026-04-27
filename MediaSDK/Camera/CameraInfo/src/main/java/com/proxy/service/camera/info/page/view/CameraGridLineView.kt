package com.proxy.service.camera.info.page.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.proxy.service.core.framework.app.resource.CsDpUtils

class CameraGridLineView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var gridEnabled = false

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(85, 255, 255, 255)
        strokeWidth = CsDpUtils.dp2pxf(1f)
    }

    fun setGridEnabled(enabled: Boolean) {
        if (gridEnabled == enabled) return
        gridEnabled = enabled
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (!gridEnabled) return

        val w = width.toFloat()
        val h = height.toFloat()

        val x1 = w / 3f
        val x2 = w * 2f / 3f
        val y1 = h / 3f
        val y2 = h * 2f / 3f

        canvas.drawLine(x1, 0f, x1, h, paint)
        canvas.drawLine(x2, 0f, x2, h, paint)
        canvas.drawLine(0f, y1, w, y1, paint)
        canvas.drawLine(0f, y2, w, y2, paint)
    }
}

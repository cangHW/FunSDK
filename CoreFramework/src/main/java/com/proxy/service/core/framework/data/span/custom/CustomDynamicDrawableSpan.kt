package com.proxy.service.core.framework.data.span.custom

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.ReplacementSpan
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.span.enums.ImageAlign
import java.lang.ref.WeakReference

/**
 * 图片展示位置适配
 *
 * @author: cangHX
 * @data: 2025/7/14 14:36
 * @desc:
 */
abstract class CustomDynamicDrawableSpan(
    private val verticalAlignment: ImageAlign
) : ReplacementSpan() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Span_DynamicDrawable"
    }

    private var mDrawableRef: WeakReference<Drawable?>? = null

    abstract fun getDrawable(): Drawable?

    private fun getCachedDrawable(): Drawable? {
        val wr = mDrawableRef
        var d: Drawable? = null
        if (wr != null) {
            d = wr.get()
        }
        if (d == null) {
            d = getDrawable()
            mDrawableRef = WeakReference(d)
        }
        return d
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val d: Drawable = getCachedDrawable() ?: return 0
        val rect = d.bounds

        if (fm == null) {
            return rect.right
        }

        val lineHeight = getLineHeightFromMetrics(paint, fm)
        val drawableHeight = rect.height()

        when (verticalAlignment) {
            ImageAlign.ALIGN_TOP -> {
                if (lineHeight < drawableHeight) {
                    fm.top = fm.top
                    fm.bottom = drawableHeight + fm.top
                    fm.ascent = fm.top
                    fm.descent = fm.bottom
                }
            }

            ImageAlign.ALIGN_CENTER -> {
                if (lineHeight < drawableHeight) {
                    fm.top = -drawableHeight / 2 - lineHeight / 4
                    fm.bottom = drawableHeight / 2 - lineHeight / 4
                    fm.ascent = fm.top
                    fm.descent = fm.bottom
                }
            }

            ImageAlign.ALIGN_BASELINE -> {
                if (lineHeight < drawableHeight + fm.bottom) {
                    fm.top = -drawableHeight
                    fm.bottom = fm.bottom
                    fm.ascent = fm.top
                    fm.descent = fm.bottom
                }
            }

            else -> {
                if (lineHeight < drawableHeight) {
                    fm.top = -drawableHeight + fm.bottom
                    fm.bottom = fm.bottom
                    fm.ascent = fm.top
                    fm.descent = fm.bottom
                }
            }
        }

        return rect.right
    }

    private fun getLineHeightFromMetrics(paint: Paint, fm: Paint.FontMetricsInt): Int {
        if (fm.top == 0 && fm.bottom == 0) {
            val fm2 = paint.fontMetricsInt
            fm.top = fm2.top
            fm.bottom = fm2.bottom
            fm.ascent = fm2.ascent
            fm.descent = fm2.descent
        }

        var lineHeight = fm.bottom - fm.top
        if (lineHeight == 0) {
            lineHeight = if (paint is TextPaint) {
                StaticLayout.Builder
                    .obtain(" ", 0, 1, paint, Int.MAX_VALUE)
                    .build()
                    .height
            } else {
                (paint.textSize * 1.2).toInt()
            }
        }

        return lineHeight
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
        val drawable = getCachedDrawable() ?: return
        val rect = drawable.bounds
        if (rect.width() <= 0 || rect.height() <= 0) {
            CsLogger.tag(TAG)
                .e("The width and height of the drawable are less than or equal to 0, so this drawable is not drawn")
            return
        }

        canvas.save()
        val lineHeight = bottom - top

        if (rect.height() < lineHeight) {
            val transY = when (verticalAlignment) {
                ImageAlign.ALIGN_TOP -> {
                    top.toFloat()
                }

                ImageAlign.ALIGN_CENTER -> {
                    (bottom + top - rect.height()) / 2f
                }

                ImageAlign.ALIGN_BASELINE -> {
                    (y - rect.height()).toFloat()
                }

                else -> {
                    (bottom - rect.height()).toFloat()
                }
            }
            canvas.translate(x, transY)
        } else {
            canvas.translate(x, top.toFloat())
        }
        drawable.draw(canvas)
        canvas.restore()
    }
}
package com.proxy.service.core.framework.data.span.custom

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
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

        val lineHeight = fm.bottom - fm.top
        if (lineHeight < rect.height()) {
            if (verticalAlignment == ImageAlign.ALIGN_TOP) {
                fm.top = fm.top
                fm.bottom = rect.height() + fm.top
            } else if (verticalAlignment == ImageAlign.ALIGN_CENTER) {
                fm.top = -rect.height() / 2 - lineHeight / 4
                fm.bottom = rect.height() / 2 - lineHeight / 4
            } else {
                fm.top = -rect.height() + fm.bottom
                fm.bottom = fm.bottom
            }
            fm.ascent = fm.top
            fm.descent = fm.bottom
        }
        return rect.right
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
        val transY: Float
        val lineHeight = bottom - top

        if (rect.height() < lineHeight) {
            transY = if (verticalAlignment == ImageAlign.ALIGN_TOP) {
                top.toFloat()
            } else if (verticalAlignment == ImageAlign.ALIGN_CENTER) {
                ((bottom + top - rect.height()) / 2).toFloat()
            } else if (verticalAlignment == ImageAlign.ALIGN_BASELINE) {
                (y - rect.height()).toFloat()
            } else {
                (bottom - rect.height()).toFloat()
            }
            canvas.translate(x, transY)
        } else {
            canvas.translate(x, top.toFloat())
        }
        drawable.draw(canvas)
        canvas.restore()
    }


}
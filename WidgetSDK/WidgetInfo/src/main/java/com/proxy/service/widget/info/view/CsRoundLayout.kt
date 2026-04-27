package com.proxy.service.widget.info.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.widget.info.R

/**
 * @author: cangHX
 * @date: 2023/11/20 11:47
 * @desc: 圆角 layout
 */
class CsRoundLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : CsMaxHeightLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val rectF = RectF()
    private val radiusF = FloatArray(8)
    private val path = Path()

    private var pathDirty = true
    private var isUniformRadius = true
    private var uniformRadius = 0f

    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    private val roundOutlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, uniformRadius)
        }
    }

    init {
        setWillNotDraw(false)
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.CsRoundLayout)

        val radius = getDimenPixelSize(array, R.styleable.CsRoundLayout_round_radius)
        val rTl = getDimenPixelSize(array, R.styleable.CsRoundLayout_round_radius_top_left)
        val rTr = getDimenPixelSize(array, R.styleable.CsRoundLayout_round_radius_top_right)
        val rBr = getDimenPixelSize(array, R.styleable.CsRoundLayout_round_radius_bottom_right)
        val rBl = getDimenPixelSize(array, R.styleable.CsRoundLayout_round_radius_bottom_left)
        array.recycle()

        var finalTL = 0f
        var finalTR = 0f
        var finalBR = 0f
        var finalBL = 0f

        if (radius >= 0) {
            finalTL = radius.toFloat()
            finalTR = radius.toFloat()
            finalBR = radius.toFloat()
            finalBL = radius.toFloat()
        }

        if (rTl >= 0) {
            finalTL = rTl.toFloat()
        }

        if (rTr >= 0) {
            finalTR = rTr.toFloat()
        }

        if (rBr >= 0) {
            finalBR = rBr.toFloat()
        }

        if (rBl >= 0) {
            finalBL = rBl.toFloat()
        }

        radiusF[0] = finalTL
        radiusF[1] = finalTL
        radiusF[2] = finalTR
        radiusF[3] = finalTR
        radiusF[4] = finalBR
        radiusF[5] = finalBR
        radiusF[6] = finalBL
        radiusF[7] = finalBL

        isUniformRadius = isUniform()
        uniformRadius = if (isUniformRadius) radiusF[0] else 0f
    }

    /**
     * 设置圆角
     * */
    fun setRadiusDp(radius: Float) {
        val r = CsDpUtils.dp2pxf(radius)
        updateRadiusF(r, r, r, r)
    }

    /**
     * 设置圆角
     * */
    fun setRadiusPx(radius: Int) {
        setRadiusPx(radius, radius, radius, radius)
    }

    /**
     * 设置圆角
     * */
    fun setRadiusDp(rTl: Float, rTr: Float, rBr: Float, rBl: Float) {
        updateRadiusF(
            CsDpUtils.dp2pxf(rTl),
            CsDpUtils.dp2pxf(rTr),
            CsDpUtils.dp2pxf(rBr),
            CsDpUtils.dp2pxf(rBl)
        )
    }

    /**
     * 设置圆角
     * */
    fun setRadiusPx(rTl: Int, rTr: Int, rBr: Int, rBl: Int) {
        updateRadiusF(rTl.toFloat(), rTr.toFloat(), rBr.toFloat(), rBl.toFloat())
    }


    private fun updateRadiusF(rTl: Float, rTr: Float, rBr: Float, rBl: Float) {
        radiusF[0] = rTl
        radiusF[1] = rTl
        radiusF[2] = rTr
        radiusF[3] = rTr
        radiusF[4] = rBr
        radiusF[5] = rBr
        radiusF[6] = rBl
        radiusF[7] = rBl
        pathDirty = true
        rebuildPathIfNeeded()
        postInvalidate()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            pathDirty = true
        }
        rebuildPathIfNeeded()
    }

    private fun rebuildPathIfNeeded() {
        if (!pathDirty || width == 0 || height == 0) {
            return
        }
        pathDirty = false
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        path.reset()
        path.addRoundRect(rectF, radiusF, Path.Direction.CCW)
        applyClippingMode()
    }

    private fun isUniform(): Boolean {
        val r = radiusF[0]
        return radiusF[2] == r && radiusF[4] == r && radiusF[6] == r
    }

    private fun applyClippingMode() {
        isUniformRadius = isUniform()
        uniformRadius = if (isUniformRadius) radiusF[0] else 0f

        if (isUniformRadius && uniformRadius > 0f) {
            outlineProvider = roundOutlineProvider
            clipToOutline = true
        } else {
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = false
        }
        invalidateOutline()
    }

    override fun draw(canvas: Canvas) {
        if (isUniformRadius) {
            super.draw(canvas)
            return
        }
        val count = canvas.saveLayer(rectF, null)
        super.draw(canvas)
        canvas.drawPath(path, maskPaint)
        canvas.restoreToCount(count)
    }
}

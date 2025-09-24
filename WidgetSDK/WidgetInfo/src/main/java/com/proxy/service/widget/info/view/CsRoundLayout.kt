package com.proxy.service.widget.info.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.proxy.service.widget.info.R

/**
 * @author: cangHX
 * @date: 2023/11/20 11:47
 * @desc:
 */
class CsRoundLayout : RelativeLayout {

    private val rectF = RectF()
    private val radiusF = FloatArray(8)
    private val path = Path()

    private var radiusLT = 0f
    private var radiusRT = 0f
    private var radiusLB = 0f
    private var radiusRB = 0f

    private var maxHeight = -1

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.CsRoundLayout)
        val radius = array.getDimensionPixelSize(R.styleable.CsRoundLayout_round_radius, -1)
        val radius_l_t =
            array.getDimensionPixelSize(R.styleable.CsRoundLayout_round_radius_top_left, -1)
        val radius_r_t =
            array.getDimensionPixelSize(R.styleable.CsRoundLayout_round_radius_top_right, -1)
        val radius_r_b =
            array.getDimensionPixelSize(R.styleable.CsRoundLayout_round_radius_bottom_right, -1)
        val radius_l_b =
            array.getDimensionPixelSize(R.styleable.CsRoundLayout_round_radius_bottom_left, -1)
        array.recycle()

        if (radius >= 0) {
            radiusLT = radius.toFloat()
            radiusRT = radius.toFloat()
            radiusLB = radius.toFloat()
            radiusRB = radius.toFloat()
        }

        if (radius_l_t >= 0) {
            radiusLT = radius_l_t.toFloat()
        }

        if (radius_r_t >= 0) {
            radiusRT = radius_r_t.toFloat()
        }

        if (radius_r_b >= 0) {
            radiusRB = radius_r_b.toFloat()
        }

        if (radius_l_b >= 0) {
            radiusLB = radius_l_b.toFloat()
        }

        radiusF[0] = radiusLT
        radiusF[1] = radiusLT
        radiusF[2] = radiusRT
        radiusF[3] = radiusRT
        radiusF[4] = radiusRB
        radiusF[5] = radiusRB
        radiusF[6] = radiusLB
        radiusF[7] = radiusLB
    }

    fun setRadius(radius: Int) {
        setRadius(radius, radius, radius, radius)
    }

    fun setRadius(radiusLT: Int, radiusRT: Int, radiusRB: Int, radiusLB: Int) {
        radiusF[0] = radiusLT.toFloat()
        radiusF[1] = radiusLT.toFloat()
        radiusF[2] = radiusRT.toFloat()
        radiusF[3] = radiusRT.toFloat()
        radiusF[4] = radiusRB.toFloat()
        radiusF[5] = radiusRB.toFloat()
        radiusF[6] = radiusLB.toFloat()
        radiusF[7] = radiusLB.toFloat()
        requestLayout()
        postInvalidate()
    }

    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSpec = heightMeasureSpec
        if (maxHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        rectF.left = 0f
        rectF.top = 0f
        rectF.right = (r - l).toFloat()
        rectF.bottom = (b - t).toFloat()

        path.reset()
        path.addRoundRect(rectF, radiusF, Path.Direction.CCW)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val num = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(num)
    }

    override fun onDrawForeground(canvas: Canvas) {
        val num = canvas.save()
        canvas.clipPath(path)
        super.onDrawForeground(canvas)
        canvas.restoreToCount(num)
    }

    override fun draw(canvas: Canvas) {
        val num = canvas.save()
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(num)
    }
}

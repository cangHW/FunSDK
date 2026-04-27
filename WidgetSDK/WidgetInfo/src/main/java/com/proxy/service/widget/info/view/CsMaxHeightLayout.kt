package com.proxy.service.widget.info.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.annotation.StyleableRes
import com.proxy.service.widget.info.R

/**
 * @author: cangHX
 * @data: 2026/4/27 16:32
 * @desc: 最大高度 layout
 */
open class CsMaxHeightLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var maxHeight = -1

    init {
        init(context, attrs)
    }

    protected fun getDimenPixelSize(array: TypedArray, @StyleableRes index: Int): Int {
        return array.getDimensionPixelSize(index, -1)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.CsMaxHeightLayout)
        maxHeight = getDimenPixelSize(array, R.styleable.CsMaxHeightLayout_max_height)
        array.recycle()
    }

    /**
     * 设置最大高度
     * */
    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
        requestLayout()
        postInvalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSpec = heightMeasureSpec
        if (maxHeight >= 0) {
            val parentSize = MeasureSpec.getSize(heightMeasureSpec)
            val parentMode = MeasureSpec.getMode(heightMeasureSpec)
            when (parentMode) {
                MeasureSpec.EXACTLY -> {
                    heightSpec = MeasureSpec.makeMeasureSpec(
                        minOf(parentSize, maxHeight), MeasureSpec.EXACTLY
                    )
                }

                MeasureSpec.AT_MOST -> {
                    heightSpec = MeasureSpec.makeMeasureSpec(
                        minOf(parentSize, maxHeight), MeasureSpec.AT_MOST
                    )
                }

                else -> {
                    heightSpec = MeasureSpec.makeMeasureSpec(
                        maxHeight, MeasureSpec.AT_MOST
                    )
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

}
package com.proxy.service.widget.info.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.widget.info.R

/**
 * @author: cangHX
 * @data: 2025/9/24 10:01
 * @desc:
 */
class CsFontStyleTextView : AppCompatTextView {

    companion object {

        private var selectTypeface: Typeface? = null

        fun setTypeface(typeface: Typeface) {
            selectTypeface = typeface
        }

        fun setTypeface(familyName: String, fontStyle: FontStyle) {
            selectTypeface = Typeface.create(familyName, fontStyle.style)
        }
    }

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
        val array = context.obtainStyledAttributes(attrs, R.styleable.CsFontStyleTextView)
        val style = array.getInt(
            R.styleable.CsFontStyleTextView_font_weight,
            FontWeight.WEIGHT_400.value
        )
        val fontStyle = array.getInt(
            R.styleable.CsFontStyleTextView_font_style,
            FontStyle.NORMAL.value
        )
        array.recycle()

        includeFontPadding = false

        typeface = getFont(style, fontStyle)
    }

    /**
     * 获取字体
     */
    private fun getFont(widget: Int, textStyle: Int): Typeface? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Typeface.create(selectTypeface, widget, textStyle == FontStyle.ITALIC.value)
        } else {
            null
        }
    }
}

enum class FontWeight(val value: Int) {
    WEIGHT_100(100),
    WEIGHT_300(300),
    WEIGHT_400(400),
    WEIGHT_500(500),
    WEIGHT_700(700),
    WEIGHT_800(800),
    WEIGHT_900(900),
}

enum class FontStyle(val value: Int, val style: Int) {
    /**
     * 正常
     * */
    NORMAL(100, Typeface.NORMAL),

    /**
     * 斜体
     * */
    ITALIC(200, Typeface.ITALIC),

    /**
     * 粗体
     * */
    BOLD(300, Typeface.BOLD),

    /**
     * 粗体 + 斜体
     * */
    BOLD_ITALIC(400, Typeface.BOLD_ITALIC);
}
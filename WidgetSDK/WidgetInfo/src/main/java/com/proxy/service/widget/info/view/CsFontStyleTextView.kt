package com.proxy.service.widget.info.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.TypefaceCompat
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.widget.info.R

/**
 * @author: cangHX
 * @data: 2025/9/24 10:01
 * @desc:
 */
class CsFontStyleTextView : AppCompatTextView {

    companion object {

        private var selectTypeface: Typeface? = null
        private val typefaceCache = CsExcellentMap<String, Typeface>()

        /**
         * 设置默认字体
         * */
        fun setDefaultTypeface(typeface: Typeface) {
            selectTypeface = typeface
        }

        /**
         * 设置默认字体
         * */
        fun setDefaultTypeface(familyName: String, fontStyle: FontStyle) {
            setDefaultTypeface(Typeface.create(familyName, fontStyle.style))
        }

        /**
         * 添加字体
         * */
        fun addTypeface(fontFamily: String, typeface: Typeface) {
            typefaceCache.putSync(fontFamily, typeface)
        }

        /**
         * 添加字体
         * */
        fun addTypeface(fontFamily: String, familyName: String, fontStyle: FontStyle) {
            addTypeface(fontFamily, Typeface.create(familyName, fontStyle.style))
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
        val fontWeight = array.getInt(
            R.styleable.CsFontStyleTextView_font_weight,
            FontWeight.WEIGHT_400.value
        )
        val fontStyle = array.getInt(
            R.styleable.CsFontStyleTextView_font_style,
            FontStyle.NORMAL.value
        )
        val fontFamily = array.getString(R.styleable.CsFontStyleTextView_font_family)
        array.recycle()

        includeFontPadding = false

        val font = if (fontFamily != null) {
            typefaceCache.get(fontFamily)
        } else {
            null
        }
        typeface = getFont(font ?: selectTypeface, fontWeight, fontStyle)
    }

    private fun getFont(typeface: Typeface?, widget: Int, style: Int): Typeface? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Typeface.create(typeface, widget, style == FontStyle.ITALIC.value)
        } else {
            try {
                TypefaceCompat.create(context, typeface, FontStyle.valueOf(style).style)
            } catch (throwable: Throwable) {
                null
            }
        }
    }
}

enum class FontWeight(val value: Int) {
    WEIGHT_50(50),
    WEIGHT_100(100),
    WEIGHT_150(150),
    WEIGHT_200(200),
    WEIGHT_250(250),
    WEIGHT_300(300),
    WEIGHT_350(350),
    WEIGHT_400(400),
    WEIGHT_450(450),
    WEIGHT_500(500),
    WEIGHT_550(550),
    WEIGHT_600(600),
    WEIGHT_650(650),
    WEIGHT_700(700),
    WEIGHT_750(750),
    WEIGHT_800(800),
    WEIGHT_850(850),
    WEIGHT_900(900),
    WEIGHT_950(950),
    WEIGHT_1000(1000);
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


    companion object {
        fun valueOf(value: Int): FontStyle {
            return when (value) {
                NORMAL.value -> {
                    NORMAL
                }

                ITALIC.value -> {
                    ITALIC
                }

                BOLD.value -> {
                    BOLD
                }

                BOLD_ITALIC.value -> {
                    BOLD_ITALIC
                }

                else -> {
                    NORMAL
                }
            }
        }
    }
}
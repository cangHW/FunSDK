package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.proxy.service.core.framework.data.span.CsSpanUtils
import com.proxy.service.core.framework.data.span.enums.BlurUnit
import com.proxy.service.core.framework.data.span.enums.ImageAlign
import com.proxy.service.core.framework.data.span.enums.TextSizeUnit
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkSpanBinding

/**
 * @author: cangHX
 * @data: 2025/7/14 16:37
 * @desc:
 */
class SpanActivity : BaseActivity<ActivityFrameworkSpanBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SpanActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.content?.let {
            show(it)
        }
    }

    private fun show(view: TextView) {
        CsSpanUtils.builder()
            .appendTxt("测试")
            .setItalic()
            .setTextColor(Color.RED)
            .setTextSize(60, TextSizeUnit.UNIT_SP)
            .setStrikethrough()

            .appendSpace(20)
            .setSpaceColor(Color.YELLOW)

            .appendTxt("文案")
            .setBold()
            .setTextColor(Color.BLUE)
            .setTextSize(60, TextSizeUnit.UNIT_SP)
            .setClick(object :View.OnClickListener{
                override fun onClick(v: View?) {

                }
            })
            .setUnderline()
            .setBlur(10f, BlurUnit.OUTER)

            .appendSpace(0)
            .setSpaceColor(Color.YELLOW)

            .appendLineEnd()

            .appendImage(R.drawable.jpg)
            .setImageAlign(ImageAlign.ALIGN_TOP)
            .setImageSize(300, 80, false)

            .appendTxt("结束")
            .setBackgroundColor(Color.WHITE)
            .setBold()
            .setTextSize(60, TextSizeUnit.UNIT_SP)
            .setSuperscript()

            .appendTxt("结束2")
            .setBackgroundColor(Color.WHITE)
            .setItalic()
            .setTextSize(60, TextSizeUnit.UNIT_SP)
            .setShadow(2f, 10f, 10f, Color.RED)
            .setSubscript()

            .createTo(view)
    }

    override fun onClick(view: View) {

    }
}
package com.proxy.service.document.pdf.view.group

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object LinearLayoutFactory : IFactory<LinearLayout> {
    override fun loadWebView(viewGroup: LinearLayout, view: View) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        viewGroup.addView(view, params)
    }
}
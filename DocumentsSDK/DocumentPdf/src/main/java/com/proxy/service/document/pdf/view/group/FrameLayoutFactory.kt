package com.proxy.service.document.pdf.view.group

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object FrameLayoutFactory : IFactory<FrameLayout> {
    override fun loadWebView(viewGroup: FrameLayout, view: View) {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        viewGroup.addView(view, params)
    }
}
package com.proxy.service.document.pdf.view.group

import android.view.View
import android.widget.RelativeLayout

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object RelativeLayoutFactory : IFactory<RelativeLayout> {
    override fun loadWebView(viewGroup: RelativeLayout, view: View) {
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        viewGroup.addView(view, params)
    }
}
package com.proxy.service.document.pdf.view.group

import android.view.View
import android.view.ViewGroup

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object ViewGroupFactory : IFactory<ViewGroup> {
    override fun loadWebView(viewGroup: ViewGroup, view: View) {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewGroup.addView(view, params)
    }
}
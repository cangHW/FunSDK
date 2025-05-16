package com.proxy.service.document.pdf.view.group

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @author: cangHX
 * @data: 2024/6/5 07:59
 * @desc:
 */
object ConstraintLayoutFactory : IFactory<ConstraintLayout> {

    override fun loadWebView(viewGroup: ConstraintLayout, view: View) {
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        viewGroup.addView(view, params)
    }
}
package com.proxy.service.widget.info.toast.info

import android.view.View
import android.widget.TextView

/**
 * @author: cangHX
 * @data: 2025/9/24 09:39
 * @desc:
 */
open class ToastViewInfo(
    rootView: View,
    private val textView: TextView?
) : BaseViewInfo(rootView) {

    override fun updateTxt(content: String) {
        textView?.setText(content)
    }

    override fun updateTxt(resId: Int) {
        textView?.setText(resId)
    }
}
package com.proxy.service.widget.info.toast.info

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * @author: cangHX
 * @data: 2025/9/24 09:40
 * @desc:
 */
open class ToastWithIconViewInfo(
    rootView: View,
    private val iconView: ImageView?,
    private val textView: TextView?
) : BaseViewInfo(rootView) {

    override fun updateIcon(bitmap: Bitmap?) {
        iconView?.setImageBitmap(bitmap)
    }

    override fun updateIcon(drawable: Drawable?) {
        iconView?.setImageDrawable(drawable)
    }

    override fun updateIcon(resId: Int) {
        iconView?.setImageResource(resId)
    }

    override fun updateTxt(content: String) {
        textView?.setText(content)
    }

    override fun updateTxt(resId: Int) {
        textView?.setText(resId)
    }
}
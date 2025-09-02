package com.proxy.service.widget.info.toast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.widget.info.R

/**
 * toast 视图工厂
 *
 * @author: cangHX
 * @data: 2025/7/8 15:53
 * @desc:
 */
open class ToastViewFactory {

    open class BaseViewInfo(val rootView: View) {

        open fun updateIcon(bitmap: Bitmap?) {}

        open fun updateIcon(drawable: Drawable?) {}

        open fun updateIcon(@DrawableRes resId: Int) {}


        open fun updateTxt(content: String) {}

        open fun updateTxt(@StringRes resId: Int) {}

    }


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

    /**
     * 获取普通 toast 视图
     * */
    @SuppressLint("MissingInflatedId")
    open fun getToastView(context: Context, tag: String): ToastViewInfo {
        val view = LayoutInflater.from(context).inflate(R.layout.cs_widget_toast_view, null)
        val textView = view.findViewById<AppCompatTextView>(R.id.toast_content)
        return ToastViewInfo(view, textView)
    }


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

    /**
     * 获取带 icon 的 toast 视图
     * */
    @SuppressLint("MissingInflatedId")
    open fun getToastViewWithIcon(context: Context, tag: String): ToastWithIconViewInfo {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.cs_widget_toast_with_icon_view, null)
        val iconView = view.findViewById<AppCompatImageView>(R.id.toast_icon)
        val textView = view.findViewById<AppCompatTextView>(R.id.toast_content)
        return ToastWithIconViewInfo(view, iconView, textView)
    }
}
package com.proxy.service.widget.info.toast

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.proxy.service.widget.info.R
import com.proxy.service.widget.info.toast.info.ToastViewInfo
import com.proxy.service.widget.info.toast.info.ToastWithIconViewInfo

/**
 * toast 视图工厂
 *
 * @author: cangHX
 * @data: 2025/7/8 15:53
 * @desc:
 */
open class ToastViewFactory {

    /**
     * 获取普通 toast 视图
     * */
    @SuppressLint("MissingInflatedId")
    open fun getToastView(context: Context, tag: String): ToastViewInfo {
        val view = LayoutInflater.from(context).inflate(R.layout.cs_widget_toast_view, null)
        val textView = view.findViewById<AppCompatTextView>(R.id.toast_content)
        return ToastViewInfo(view, textView)
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
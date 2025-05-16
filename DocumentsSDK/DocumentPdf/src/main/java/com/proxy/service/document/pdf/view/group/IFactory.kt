package com.proxy.service.document.pdf.view.group

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @author: cangHX
 * @data: 2024/6/5 07:59
 * @desc:
 */
interface IFactory<T : ViewGroup> {

    fun loadWebView(viewGroup: T, view: View)

    companion object {
        fun of(viewGroup: ViewGroup, view: View) {
            when (viewGroup) {
                is LinearLayout -> {
                    LinearLayoutFactory.loadWebView(viewGroup, view)
                }

                is RelativeLayout -> {
                    RelativeLayoutFactory.loadWebView(viewGroup, view)
                }

                is FrameLayout -> {
                    FrameLayoutFactory.loadWebView(viewGroup, view)
                }

                is ConstraintLayout -> {
                    ConstraintLayoutFactory.loadWebView(viewGroup, view)
                }

                else -> {
                    ViewGroupFactory.loadWebView(viewGroup, view)
                }
            }
        }
    }

}
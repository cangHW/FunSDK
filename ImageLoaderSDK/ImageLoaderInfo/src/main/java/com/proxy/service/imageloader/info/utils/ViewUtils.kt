package com.proxy.service.imageloader.info.utils

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @author: cangHX
 * @data: 2025/10/10 18:04
 * @desc:
 */
object ViewUtils {

    fun addView(view: View, viewGroup: ViewGroup, width: Int, height: Int) {
        when (viewGroup) {
            is LinearLayout -> {
                addViewToLinearLayout(view, viewGroup, width, height)
            }

            is RelativeLayout -> {
                addViewToRelativeLayout(view, viewGroup, width, height)
            }

            is FrameLayout -> {
                addViewToFrameLayout(view, viewGroup, width, height)
            }

            is ConstraintLayout -> {
                addViewToConstraintLayout(view, viewGroup, width, height)
            }

            else -> {
                addViewToViewGroup(view, viewGroup, width, height)
            }
        }
    }

    private fun addViewToConstraintLayout(
        view: View,
        viewGroup: ViewGroup,
        width: Int,
        height: Int
    ) {
        val params = ConstraintLayout.LayoutParams(width, height)
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        viewGroup.addView(view, params)
    }

    private fun addViewToFrameLayout(
        view: View,
        viewGroup: ViewGroup,
        width: Int,
        height: Int
    ) {
        val params = FrameLayout.LayoutParams(width, height)
        params.gravity = Gravity.CENTER
        viewGroup.addView(view, params)
    }

    private fun addViewToLinearLayout(
        view: View,
        viewGroup: ViewGroup,
        width: Int,
        height: Int
    ) {
        val params = LinearLayout.LayoutParams(width, height)
        params.gravity = Gravity.CENTER
        viewGroup.addView(view, params)
    }

    private fun addViewToRelativeLayout(
        view: View,
        viewGroup: ViewGroup,
        width: Int,
        height: Int
    ) {
        val params = RelativeLayout.LayoutParams(width, height)
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        viewGroup.addView(view, params)
    }

    private fun addViewToViewGroup(
        view: View,
        viewGroup: ViewGroup,
        width: Int,
        height: Int
    ) {
        val params = ViewGroup.LayoutParams(width, height)
        viewGroup.addView(view, params)
    }
}
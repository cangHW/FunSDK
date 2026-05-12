package com.proxy.service.document.image.info.func.preview.factory

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.constants.ImageConstants

/**
 * @author: cangHX
 * @date: 2025/5/30 18:44
 * @desc:
 */
object LayoutFactory {

    private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}LayoutFactory"

    fun loadImageView(viewGroup: ViewGroup): ImageView {
        if (viewGroup.childCount > 0) {
            CsLogger.tag(TAG).d("Clear ViewGroup children before loading image. childCount=${viewGroup.childCount}")
        }
        viewGroup.removeAllViews()
        val imageView = ImageView(viewGroup.context)
        val params = when (viewGroup) {
            is LinearLayout -> {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                params.gravity = Gravity.CENTER
                params
            }

            is RelativeLayout -> {
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                params
            }

            is FrameLayout -> {
                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                params.gravity = Gravity.CENTER
                params
            }

            is ConstraintLayout -> {
                val params = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                params
            }

            else -> {
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
        viewGroup.addView(imageView, params)
        return imageView
    }

}
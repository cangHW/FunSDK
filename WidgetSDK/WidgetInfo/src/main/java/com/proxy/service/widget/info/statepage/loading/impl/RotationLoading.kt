package com.proxy.service.widget.info.statepage.loading.impl

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.proxy.service.widget.info.databinding.CsWidgetStatePageLoadingRotationBinding
import com.proxy.service.widget.info.statepage.config.PageConfig
import com.proxy.service.widget.info.statepage.loading.LoadingController

/**
 * @author: cangHX
 * @data: 2025/7/10 10:33
 * @desc:
 */
class RotationLoading : LoadingController {

    companion object {
        private const val BG_START_COLOR = "#00000000"
    }

    private var binding: CsWidgetStatePageLoadingRotationBinding? = null

    private var animator: ObjectAnimator? = null
    private var valueAnimator: ValueAnimator? = null

    override fun initView(viewGroup: ViewGroup) {
        binding = CsWidgetStatePageLoadingRotationBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            true
        )
        binding?.root?.setOnClickListener {  }
        hide()
    }

    override fun show(any: Any?) {
        binding?.root?.visibility = View.VISIBLE
        createAnim(binding?.csStatePageLoadingIcon)?.start()
        createBgAnim(binding?.root)?.start()
    }

    override fun hide() {
        binding?.root?.visibility = View.GONE
        createAnim(binding?.csStatePageLoadingIcon)?.cancel()
        createBgAnim(binding?.root)?.cancel()
    }

    private fun createAnim(view: AppCompatImageView?): ObjectAnimator? {
        if (view == null) {
            return null
        }
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
            animator?.duration = 1200
            animator?.repeatCount = ObjectAnimator.INFINITE
            animator?.interpolator = LinearInterpolator()
        }
        return animator
    }

    private fun createBgAnim(view: View?): ValueAnimator? {
        if (view == null) {
            return null
        }
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofArgb(
                Color.parseColor(BG_START_COLOR),
                Color.parseColor(PageConfig.background_loading)
            )
            valueAnimator?.duration = 180
            valueAnimator?.addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Int
                view.setBackgroundColor(animatedValue)
            }
        }
        return valueAnimator
    }
}
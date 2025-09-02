package com.proxy.service.widget.info.statepage.error.impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.proxy.service.widget.info.databinding.CsWidgetStatePageErrorWithrefreshBinding
import com.proxy.service.widget.info.statepage.error.ErrorController

/**
 * @author: cangHX
 * @data: 2025/7/10 18:10
 * @desc:
 */
class WithRefreshError : ErrorController {

    private var binding: CsWidgetStatePageErrorWithrefreshBinding? = null

    override fun initView(viewGroup: ViewGroup) {
        binding = CsWidgetStatePageErrorWithrefreshBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            true
        )
        binding?.root?.setOnClickListener { }
        hide()
    }

    override fun show(
        message: String?,
        buttonTxt: String?,
        any: Any?,
        buttonClick: (() -> Unit)?
    ) {
        binding?.root?.visibility = View.VISIBLE

        message?.let {
            binding?.csStatePageErrorTxt?.setText(it)
        }

        buttonTxt?.let {
            binding?.csStatePageErrorButton?.setText(it)
        }

        binding?.csStatePageErrorButton?.setOnClickListener {
            if (buttonClick != null) {
                buttonClick()
            }
        }
    }

    override fun hide() {
        binding?.root?.visibility = View.GONE
    }
}
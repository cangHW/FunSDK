package com.proxy.service.widget.info.statepage.empty.impl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.proxy.service.widget.info.databinding.CsWidgetStatePageEmptyWithoutrefreshBinding
import com.proxy.service.widget.info.statepage.empty.EmptyController

/**
 * @author: cangHX
 * @data: 2025/7/10 17:26
 * @desc:
 */
class WithOutRefreshEmpty : EmptyController {

    private var binding: CsWidgetStatePageEmptyWithoutrefreshBinding? = null

    override fun initView(viewGroup: ViewGroup) {
        binding = CsWidgetStatePageEmptyWithoutrefreshBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            true
        )
        binding?.root?.setOnClickListener { }
        hide()
    }

    override fun show(message: String?, any: Any?, buttonClick: (() -> Unit)?) {
        binding?.root?.visibility = View.VISIBLE

        message?.let {
            binding?.csStatePageEmptyTxt?.setText(message)
        }
    }

    override fun hide() {
        binding?.root?.visibility = View.GONE
    }
}
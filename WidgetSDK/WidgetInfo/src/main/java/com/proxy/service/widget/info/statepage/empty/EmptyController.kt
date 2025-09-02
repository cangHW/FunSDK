package com.proxy.service.widget.info.statepage.empty

import android.view.ViewGroup

/**
 * @author: cangHX
 * @data: 2025/7/10 10:01
 * @desc:
 */
interface EmptyController {

    /**
     * 初始化
     * */
    fun initView(viewGroup: ViewGroup)

    /**
     * 显示页面
     *
     * @param message       信息
     * @param any           自定义数据
     * @param buttonClick   按钮点击
     * */
    fun show(message: String? = null, any: Any? = null, buttonClick: (() -> Unit)? = null)

    /**
     * 隐藏页面
     * */
    fun hide()
}
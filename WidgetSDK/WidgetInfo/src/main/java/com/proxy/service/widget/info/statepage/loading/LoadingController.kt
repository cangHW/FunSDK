package com.proxy.service.widget.info.statepage.loading

import android.view.ViewGroup

/**
 * @author: cangHX
 * @data: 2025/7/9 20:05
 * @desc:
 */
interface LoadingController {

    /**
     * 初始化
     * */
    fun initView(viewGroup: ViewGroup)

    /**
     * 显示
     *
     * @param any   自定义数据
     * */
    fun show(any: Any?)

    /**
     * 隐藏
     * */
    fun hide()

}
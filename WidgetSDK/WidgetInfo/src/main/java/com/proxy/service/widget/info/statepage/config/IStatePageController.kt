package com.proxy.service.widget.info.statepage.config

import android.view.View

/**
 * @author: cangHX
 * @data: 2025/7/9 20:06
 * @desc:
 */
interface IStatePageController {

    /**
     * 根 view
     */
    fun getRootView(): View

    /**
     * 加载成功, 显示正常页面, 自动隐藏全部状态页面(loading、error、empty)
     * */
    fun showSuccess()

    /**
     * 显示 loading
     *
     * @param any   自定义数据
     * */
    fun showLoading(any: Any? = null)

    /**
     * 隐藏 loading
     * */
    fun hideLoading()

    /**
     * 显示错误页面
     *
     * @param message       错误信息
     * @param buttonTxt     按钮文案
     * @param any           自定义数据
     * @param buttonClick   按钮点击
     * */
    fun showError(
        message: String? = null,
        buttonTxt: String? = null,
        any: Any? = null,
        buttonClick: (() -> Unit)? = null
    )

    /**
     * 隐藏错误页面
     * */
    fun hideError()

    /**
     * 显示空数据页面
     *
     * @param message       空数据信息
     * @param any           自定义数据
     * @param buttonClick   按钮点击
     * */
    fun showEmpty(message: String? = null, any: Any? = null, buttonClick: (() -> Unit)? = null)

    /**
     * 隐藏空数据页面
     * */
    fun hideEmpty()
}
package com.proxy.service.widget.info.dialog.window.base

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * @author: cangHX
 * @data: 2025/11/27 16:57
 * @desc:
 */
interface ILifecycleDialog {

    /**
     * 创建
     * */
    fun onCreate(context: Context)

    /**
     * 创建 view
     * */
    fun onCreateView(context: Context, parent: ViewGroup): View?

    /**
     * 开始
     * */
    fun onStart()

    /**
     * 停止
     * */
    fun onStop()

    /**
     * 销毁 view
     * */
    fun onDestroyView()

    /**
     * 销毁
     * */
    fun onDestroy()

}
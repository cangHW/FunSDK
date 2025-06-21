package com.proxy.service.document.pdf.base.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback

/**
 * @author: cangHX
 * @data: 2025/5/15 09:42
 * @desc:
 */
interface IViewFactory {

    /**
     * 设置视图背景色, 默认白色, 格式为：0xAARRGGBB
     * */
    fun setViewBackgroundColor(color: Long): IViewFactory

    /**
     * 设置页面背景色, 默认白色, 格式为：0xAARRGGBB
     * */
    fun setPageBackgroundColor(color: Long): IViewFactory

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): IViewFactory

    /**
     * 设置加载回调
     * */
    fun setLoadStateCallback(callback: LoadStateCallback):IViewFactory

    /**
     * 创建控制器并加载到父视图
     * */
    fun into(viewGroup: ViewGroup): IPdfView
}
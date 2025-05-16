package com.proxy.service.document.base.pdf.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

/**
 * @author: cangHX
 * @data: 2025/5/15 09:42
 * @desc:
 */
interface IViewFactory {

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): IViewFactory

    /**
     * 创建控制器并加载到父视图
     * */
    fun into(viewGroup: ViewGroup): IPdfView
}
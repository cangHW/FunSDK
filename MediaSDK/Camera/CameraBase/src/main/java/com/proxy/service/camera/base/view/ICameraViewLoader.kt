package com.proxy.service.camera.base.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

/**
 * @author: cangHX
 * @data: 2026/2/4 16:04
 * @desc:
 */
interface ICameraViewLoader {

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): ICameraViewLoader

    /**
     * 创建相机 view 到 ViewGroup
     * */
    fun createTo(viewGroup: ViewGroup?): IView

}
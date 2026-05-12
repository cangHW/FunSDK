package com.proxy.service.document.image.base.loader.base

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * 资源绑定接口, 用于将已选择的图片资源加载到指定 View.
 *
 * @author: cangHX
 * @date: 2025/5/30 10:14
 * @desc:
 */
interface ILoader<T> {

    /**
     * 加载资源到 imageview
     * */
    fun into(imageView: ImageView): T

    /**
     * 加载资源到 LinearLayout, 会先清空容器内所有子 View
     * */
    fun into(linearLayout: LinearLayout): T

    /**
     * 加载资源到 RelativeLayout, 会先清空容器内所有子 View
     * */
    fun into(relativeLayout: RelativeLayout): T

    /**
     * 加载资源到 FrameLayout, 会先清空容器内所有子 View
     * */
    fun into(frameLayout: FrameLayout): T

    /**
     * 加载资源到 ViewGroup, 会先清空容器内所有子 View
     * */
    fun into(viewGroup: ViewGroup): T

}
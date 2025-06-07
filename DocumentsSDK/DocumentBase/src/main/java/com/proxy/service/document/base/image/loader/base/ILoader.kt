package com.proxy.service.document.base.image.loader.base

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * @author: cangHX
 * @data: 2025/5/30 10:14
 * @desc:
 */
interface ILoader<T> {

    /**
     * 加载资源到 imageview
     * */
    fun into(imageView: ImageView): T

    /**
     * 加载资源到 LinearLayout
     * */
    fun into(linearLayout: LinearLayout): T

    /**
     * 加载资源到 RelativeLayout
     * */
    fun into(relativeLayout: RelativeLayout): T

    /**
     * 加载资源到 FrameLayout
     * */
    fun into(frameLayout: FrameLayout): T

    /**
     * 加载资源到 ViewGroup
     * */
    fun into(viewGroup: ViewGroup): T

}
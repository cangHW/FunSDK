package com.proxy.service.document.base.image.loader

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
interface ILoader {

    /**
     * 加载资源到 imageview
     * */
    fun into(imageView: ImageView): IController

    /**
     * 加载资源到 LinearLayout
     * */
    fun into(linearLayout: LinearLayout): IController

    /**
     * 加载资源到 RelativeLayout
     * */
    fun into(relativeLayout: RelativeLayout): IController

    /**
     * 加载资源到 FrameLayout
     * */
    fun into(frameLayout: FrameLayout): IController

    /**
     * 加载资源到 ViewGroup
     * */
    fun into(viewGroup: ViewGroup): IController

}
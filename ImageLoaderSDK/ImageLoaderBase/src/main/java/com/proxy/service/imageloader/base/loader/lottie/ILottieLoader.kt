package com.proxy.service.imageloader.base.loader.lottie

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * @author: cangHX
 * @data: 2024/6/4 17:40
 * @desc:
 */
interface ILottieLoader {

    /**
     * 加载资源到 LinearLayout
     * */
    fun into(linearLayout: LinearLayout?): LottieController

    /**
     * 加载资源到 RelativeLayout
     * */
    fun into(relativeLayout: RelativeLayout?): LottieController

    /**
     * 加载资源到 FrameLayout
     * */
    fun into(frameLayout: FrameLayout?): LottieController

    /**
     * 加载资源到 ViewGroup
     * */
    fun into(viewGroup: ViewGroup?): LottieController

}
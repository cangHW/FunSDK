package com.proxy.service.imageloader.base.loader.pag

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * @author: cangHX
 * @data: 2025/10/10 15:02
 * @desc:
 */
interface IPagLoader {

    /**
     * 加载资源到 LinearLayout
     * */
    fun into(linearLayout: LinearLayout?): PagController

    /**
     * 加载资源到 RelativeLayout
     * */
    fun into(relativeLayout: RelativeLayout?): PagController

    /**
     * 加载资源到 FrameLayout
     * */
    fun into(frameLayout: FrameLayout?): PagController

    /**
     * 加载资源到 ViewGroup
     * */
    fun into(viewGroup: ViewGroup?): PagController

}
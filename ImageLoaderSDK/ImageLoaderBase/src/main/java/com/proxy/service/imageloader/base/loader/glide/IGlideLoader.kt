package com.proxy.service.imageloader.base.loader.glide

import android.widget.ImageView
import com.proxy.service.imageloader.base.target.CsCustomTarget

/**
 * 图片加载器
 *
 * @author: cangHX
 * @data: 2024/5/16 09:50
 * @desc:
 */
interface IGlideLoader<R> {

    /**
     * 加载资源到 imageview
     * */
    fun into(imageView: ImageView?)

    /**
     * 加载资源到 imageview
     * */
    fun into(target: CsCustomTarget<R>?)

}
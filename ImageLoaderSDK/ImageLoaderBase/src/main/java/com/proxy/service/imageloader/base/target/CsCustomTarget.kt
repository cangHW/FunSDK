package com.proxy.service.imageloader.base.target

import android.graphics.drawable.Drawable

/**
 * 加载转换器
 *
 * @author: cangHX
 * @data: 2024/5/16 17:48
 * @desc:
 */
abstract class CsCustomTarget<R> {

    private var width = 0
    private var height = 0

    constructor() : this(Int.MIN_VALUE, Int.MIN_VALUE)

    constructor(width: Int, height: Int) {
        this.width = if (width <= 0) {
            Int.MIN_VALUE
        } else {
            width
        }
        this.height = if (height <= 0) {
            Int.MIN_VALUE
        } else {
            height
        }
    }

    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }

    /**
     * 生命周期，start
     * */
    open fun onStart() {

    }

    /**
     * 生命周期，stop
     * */
    open fun onStop() {

    }

    /**
     * 生命周期，destroy
     * */
    open fun onDestroy() {

    }

    /**
     * 资源开始加载
     * */
    open fun onLoadStarted(placeholder: Drawable?) {

    }

    /**
     * 资源加载失败
     * */
    open fun onLoadFailed(errorDrawable: Drawable?) {

    }

    /**
     * 资源加载完成
     * */
    abstract fun onResourceReady(resource: R?)

    /**
     * 资源被清除
     * */
    abstract fun onLoadCleared(placeholder: Drawable?)

}
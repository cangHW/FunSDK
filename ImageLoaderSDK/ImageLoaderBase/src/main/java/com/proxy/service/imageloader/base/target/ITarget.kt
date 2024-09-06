package com.proxy.service.imageloader.base.target

import android.graphics.drawable.Drawable

/**
 * date: 2024/5/16 17:48
 * author: MaJi
 * desc: 返回图片数据
 */
interface ITarget<R> {

    /**
     * 生命周期，start
     * */
    fun onStart() {

    }

    /**
     * 生命周期，stop
     * */
    fun onStop() {

    }

    /**
     * 生命周期，destroy
     * */
    fun onDestroy() {

    }

    /**
     * 资源开始加载
     * */
    fun onLoadStarted(placeholder: Drawable?) {

    }

    /**
     * 资源加载失败
     * */
    fun onLoadFailed(errorDrawable: Drawable?) {

    }

    /**
     * 资源加载完成
     * */
    fun onResourceReady(resource: R?)

    /**
     * 资源被清除
     * */
    fun onLoadCleared(placeholder: Drawable?)

}
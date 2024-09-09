package com.proxy.service.core.service.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.imageloader.base.ImageLoaderService
import com.proxy.service.imageloader.base.type.IType

/**
 * @author: cangHX
 * @data: 2024/5/14 16:50
 * @desc: 图片 or 动画加载框架入口
 */
object CsImgLoader {

    private var service: ImageLoaderService? = null

    private fun getService(): ImageLoaderService? {
        if (service == null) {
            service = CloudSystem.getService(ImageLoaderService::class.java)
        }
        if (service == null) {
            CsLogger.e("Please check to see if it is referenced. <io.github.cangHW:Service-ImageloaderBase:xxx>")
        }
        return service
    }

    /**
     * 绑定上下文生命周期，并获取图片加载请求链路
     * */
    fun with(activity: FragmentActivity): IType<Drawable>? {
        return getService()?.with(activity)
    }

    /**
     * 绑定上下文生命周期，并获取图片加载请求链路
     * */
    fun with(fragment: Fragment): IType<Drawable>? {
        return getService()?.with(fragment)
    }

    /**
     * 绑定上下文生命周期，并获取图片加载请求链路
     * */
    fun with(context: Context): IType<Drawable>? {
        return getService()?.with(context)
    }

    /**
     * 绑定上下文生命周期，并获取图片加载请求链路
     * */
    fun with(view: View): IType<Drawable>? {
        return getService()?.with(view)
    }

}
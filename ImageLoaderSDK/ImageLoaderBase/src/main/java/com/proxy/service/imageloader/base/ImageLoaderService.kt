package com.proxy.service.imageloader.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.base.BaseService
import com.proxy.service.imageloader.base.type.IType

/**
 * @author: cangHX
 * @data: 2024/5/15 20:23
 * @desc:
 */
interface ImageLoaderService : BaseService {

    /**
     * 绑定上下文环境与生命周期
     * */
    fun with(activity: FragmentActivity): IType<Drawable>

    /**
     * 绑定上下文环境与生命周期
     * */
    fun with(fragment: Fragment): IType<Drawable>

    /**
     * 绑定上下文环境与生命周期
     * */
    fun with(view: View): IType<Drawable>

    /**
     * 绑定上下文环境与生命周期
     * */
    fun with(context: Context): IType<Drawable>

}
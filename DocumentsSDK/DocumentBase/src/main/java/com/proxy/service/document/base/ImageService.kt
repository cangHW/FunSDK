package com.proxy.service.document.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.base.BaseService
import com.proxy.service.document.base.image.loader.IRequest

/**
 * @author: cangHX
 * @date: 2025/4/29 21:33
 * @desc:
 */
interface ImageService : BaseService {

    /**
     * 创建 image 加载器
     *
     * */
    fun createLoader(activity: FragmentActivity): IRequest

    /**
     * 创建 image 加载器
     *
     * */
    fun createLoader(fragment: Fragment): IRequest

    /**
     * 创建 image 加载器
     *
     * */
    fun createLoader(context: Context): IRequest

//    fun
}
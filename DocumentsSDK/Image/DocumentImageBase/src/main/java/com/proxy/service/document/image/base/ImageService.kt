package com.proxy.service.document.image.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.base.BaseService
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.base.IRequest
import com.proxy.service.document.image.base.loader.crop.ICropOption

/**
 * @author: cangHX
 * @date: 2025/4/29 21:33
 * @desc:
 */
interface ImageService : BaseService {

    /*** *** *** *** *** *** 预览 *** *** *** *** *** ***/

    /**
     * 创建预览加载器
     * */
    fun createPreviewLoader(activity: FragmentActivity): IRequest<IOption>

    /**
     * 创建预览加载器
     * */
    fun createPreviewLoader(fragment: Fragment): IRequest<IOption>

    /**
     * 创建预览加载器
     * */
    fun createPreviewLoader(context: Context): IRequest<IOption>

    /*** *** *** *** *** *** 裁剪 *** *** *** *** *** ***/

    /**
     * 创建裁剪加载器, 该模式能配置更多参数, 但受限于不同裁剪模式效果
     *
     * @param request   预览加载器
     *
     * */
    fun createCropLoader(request: IRequest<IOption>): IRequest<ICropOption>

    /**
     * 创建裁剪加载器
     * */
    fun createCropLoader(activity: FragmentActivity): IRequest<ICropOption>

    /**
     * 创建裁剪加载器
     * */
    fun createCropLoader(fragment: Fragment): IRequest<ICropOption>

    /**
     * 创建裁剪加载器
     * */
    fun createCropLoader(context: Context): IRequest<ICropOption>
}
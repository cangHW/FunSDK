package com.proxy.service.imageloader.info

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.imageloader.base.ImageLoaderService
import com.proxy.service.imageloader.base.type.IType
import com.proxy.service.imageloader.info.info.glide.GlideInfo
import com.proxy.service.imageloader.info.type.TypeImpl
import com.proxy.service.imageloader.info.type.type.DrawableType

/**
 * @author: cangHX
 * @data: 2024/5/16 09:52
 * @desc:
 */
@CloudApiService(serviceTag = "service/image_loader")
class ImageLoaderServiceImpl : ImageLoaderService {

    override fun with(activity: FragmentActivity): IType<Drawable> {
        val info = getGlideInfo()
        info.sourceType = DrawableType()
        info.activity = activity
        return TypeImpl(info)
    }

    override fun with(fragment: Fragment): IType<Drawable> {
        val info = getGlideInfo()
        info.sourceType = DrawableType()
        info.fragment = fragment
        return TypeImpl(info)
    }

    override fun with(view: View): IType<Drawable> {
        val info = getGlideInfo()
        info.sourceType = DrawableType()
        info.view = view
        return TypeImpl(info)
    }

    override fun with(context: Context): IType<Drawable> {
        val info = getGlideInfo()
        info.sourceType = DrawableType()
        info.ctx = context
        return TypeImpl(info)
    }

    private fun getGlideInfo(): GlideInfo<Drawable> {
        return GlideInfo()
    }
}
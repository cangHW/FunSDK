package com.proxy.service.document.image.info

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.document.image.base.ImageService
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.base.IRequest
import com.proxy.service.document.image.base.loader.crop.ICropOption
import com.proxy.service.document.image.info.func.preview.RequestImpl
import com.proxy.service.document.image.info.func.crop.CropRequestImpl

/**
 * @author: cangHX
 * @data: 2025/5/30 10:46
 * @desc:
 */
@CloudApiService(serviceTag = "service/image")
class ImageServiceImage : ImageService {

    override fun createPreviewLoader(activity: FragmentActivity): IRequest<IOption> {
        val glideRequest = CsImageLoader.with(activity)?.asBitmapModel()
        return RequestImpl(glideRequest)
    }

    override fun createPreviewLoader(fragment: Fragment): IRequest<IOption> {
        val glideRequest = CsImageLoader.with(fragment)?.asBitmapModel()
        return RequestImpl(glideRequest)
    }

    override fun createPreviewLoader(context: Context): IRequest<IOption> {
        val glideRequest = CsImageLoader.with(context)?.asBitmapModel()
        return RequestImpl(glideRequest)
    }

    override fun createCropLoader(activity: FragmentActivity): IRequest<ICropOption> {
        return CropRequestImpl(createPreviewLoader(activity))
    }

    override fun createCropLoader(fragment: Fragment): IRequest<ICropOption> {
        return CropRequestImpl(createPreviewLoader(fragment))
    }

    override fun createCropLoader(context: Context): IRequest<ICropOption> {
        return CropRequestImpl(createPreviewLoader(context))
    }
}
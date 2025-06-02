package com.proxy.service.document.image

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.document.base.ImageService
import com.proxy.service.document.base.image.loader.IRequest
import com.proxy.service.document.image.loader.RequestImpl

/**
 * @author: cangHX
 * @data: 2025/5/30 10:46
 * @desc:
 */
@CloudApiService(serviceTag = "service/image")
class ImageServiceImage : ImageService {

    override fun createLoader(activity: FragmentActivity): IRequest {
        val glideRequest = CsImageLoader.with(activity)?.asBitmapModel()
        return RequestImpl(glideRequest)
    }

    override fun createLoader(fragment: Fragment): IRequest {
        val glideRequest = CsImageLoader.with(fragment)?.asBitmapModel()
        return RequestImpl(glideRequest)
    }

    override fun createLoader(context: Context): IRequest {
        val glideRequest = CsImageLoader.with(context)?.asBitmapModel()
        return RequestImpl(glideRequest)
    }
}
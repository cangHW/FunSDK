package com.proxy.service.core.service.document

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.ImageService
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.base.IRequest
import com.proxy.service.document.image.base.loader.crop.ICropOption

/**
 * 文档-图片 加载框架入口
 *
 * @author: cangHX
 * @data: 2025/6/7 14:45
 * @desc:
 */
object CsDocumentImage {

    private const val TAG = "${CoreConfig.TAG}DocumentImage"

    private var service: ImageService? = null

    private fun getService(): ImageService? {
        if (service == null) {
            service = CloudSystem.getService(ImageService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.cangHW:Service-Document-Image:xxx>")
        }
        return service
    }

    /**
     * 创建预览加载器
     * */
    fun createPreviewLoader(activity: FragmentActivity): IRequest<IOption>? {
        return getService()?.createPreviewLoader(activity)
    }

    /**
     * 创建预览加载器
     * */
    fun createPreviewLoader(fragment: Fragment): IRequest<IOption>? {
        return getService()?.createPreviewLoader(fragment)
    }

    /**
     * 创建预览加载器
     * */
    fun createPreviewLoader(context: Context): IRequest<IOption>? {
        return getService()?.createPreviewLoader(context)
    }

    /**
     * 创建裁剪加载器, 该模式能配置更多参数, 但受限于不同裁剪模式效果
     *
     * @param request   预览加载器
     *
     * */
    fun createCropLoader(request: IRequest<IOption>): IRequest<ICropOption>? {
        return getService()?.createCropLoader(request)
    }

    /**
     * 创建裁剪加载器
     * */
    fun createCropLoader(activity: FragmentActivity): IRequest<ICropOption>? {
        return getService()?.createCropLoader(activity)
    }

    /**
     * 创建裁剪加载器
     * */
    fun createCropLoader(fragment: Fragment): IRequest<ICropOption>? {
        return getService()?.createCropLoader(fragment)
    }

    /**
     * 创建裁剪加载器
     * */
    fun createCropLoader(context: Context): IRequest<ICropOption>? {
        return getService()?.createCropLoader(context)
    }

}
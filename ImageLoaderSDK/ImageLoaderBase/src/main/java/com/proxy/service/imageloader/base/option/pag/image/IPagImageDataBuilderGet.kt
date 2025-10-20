package com.proxy.service.imageloader.base.option.pag.image

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants

/**
 * @author: cangHX
 * @data: 2025/10/16 17:05
 * @desc:
 */
interface IPagImageDataBuilderGet {

    /**
     * 获取 bitmap
     * */
    fun getImageBitmap(): Bitmap?

    /**
     * 获取图片在线链接
     * */
    fun getImageUrl(): String?

    /**
     * 获取在线图片的缓存 key
     * */
    fun getImageUrlCacheKey(): String?

    /**
     * 获取图片的 AssetsName
     * */
    fun getImageAssetsName(): String?

    /**
     * 获取图片本地地址
     * */
    fun getImagePath(): String?

    /**
     * 获取图片的字节数组
     * */
    fun getImageByte(): ByteArray?

    /**
     * 获取图片的资源 id
     * */
    @RawRes
    @DrawableRes
    fun getImageRes(): Int?

    /**
     * 获取缩放模式
     * */
    fun getScaleMode(): PagScaleMode

    /**
     * 获取加载失败占位图
     * */
    fun getErrorDrawableRes(): Int?

    /**
     * 当图像加载失败时, 是否允许动画继续执行
     * */
    fun allowAnimationOnImageFailure(): Boolean
}
package com.proxy.service.imageloader.info.pag.option.config.source.image

import android.text.TextUtils
import com.proxy.service.imageloader.info.pag.net.NetFactory
import com.proxy.service.imageloader.info.pag.net.RequestCallback
import com.proxy.service.imageloader.info.pag.option.config.RequestFailedException
import com.proxy.service.imageloader.info.utils.StringUtils
import org.libpag.PAGImage

/**
 * @author: cangHX
 * @data: 2025/10/16 18:07
 * @desc:
 */
class ImageUrlSource(
    private val url: String,
    private val cacheKey: String?
) : BaseImageSource() {

    companion object {
        private const val CACHE_KEY_POSTFIX = "image_cache_"
    }

    private val lock = Any()

    @Volatile
    private var path: String? = null

    @Volatile
    private var exception: Throwable? = null

    @Volatile
    private var callback: IImageLoadCallback? = null

    override fun preLoad() {
        if (TextUtils.isEmpty(url)) {
            exception = IllegalArgumentException("The image url cannot be empty. url=$url")
            return
        }
        val cacheKey = if (TextUtils.isEmpty(cacheKey)) {
            "${CACHE_KEY_POSTFIX}${StringUtils.urlToString(url)}"
        } else {
            "${CACHE_KEY_POSTFIX}$cacheKey"
        }
        try {
            NetFactory.enqueue(cacheKey, url, object : RequestCallback {
                override fun onSuccess(path: String) {
                    this@ImageUrlSource.path = path
                    tryCall()
                }

                override fun onFailed() {
                    this@ImageUrlSource.exception = RequestFailedException()
                    tryCall()
                }
            })
        } catch (throwable: Throwable) {
            this.exception = throwable
            tryCall()
        }
    }

    override fun loadImage(callback: IImageLoadCallback) {
        if (TextUtils.isEmpty(url)) {
            callback.onError(IllegalArgumentException("The image url cannot be empty. url=$url"))
            return
        }

        this.callback = callback
        tryCall()
    }

    private fun tryCall() {
        synchronized(lock) {
            val cb = callback ?: return

            path?.let {
                try {
                    val image = PAGImage.FromPath(path)
                    if (image == null) {
                        cb.onError(IllegalArgumentException("The image loading failed. path=$path"))
                        return
                    }
                    cb.onResult(image)
                } catch (throwable: Throwable) {
                    cb.onError(throwable)
                }
                return@synchronized
            }

            exception?.let {
                if (it is RequestFailedException) {
                    cb.onError(null)
                } else {
                    cb.onError(it)
                }
            }
        }
    }

    override fun toString(): String {
        return "ImageUrlSource(url=$url, cacheKey=$cacheKey)"
    }
}
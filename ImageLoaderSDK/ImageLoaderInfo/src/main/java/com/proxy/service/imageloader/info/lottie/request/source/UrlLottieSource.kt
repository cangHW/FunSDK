package com.proxy.service.imageloader.info.lottie.request.source

import android.content.Context
import android.text.TextUtils
import com.proxy.service.imageloader.info.lottie.net.LottieNet
import com.proxy.service.imageloader.info.net.RequestCallback
import com.proxy.service.imageloader.info.utils.StringUtils

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class UrlLottieSource(
    private val url: String,
    private val cacheKey: String?
) : BaseLottieSourceData() {

    companion object {
        private const val CACHE_KEY_POSTFIX = "lottie_cache_"
    }

    override fun load(context: Context?, listener: ILottieLoadCallback?) {
        val key = if (TextUtils.isEmpty(cacheKey)) {
            "${CACHE_KEY_POSTFIX}${StringUtils.urlToString(url)}"
        } else {
            "${CACHE_KEY_POSTFIX}$cacheKey"
        }

        try {
            LottieNet.request(key, url, object : RequestCallback {
                override fun onSuccess(path: String) {
                    val pathLottieSource = PathLottieSource(path)
                    pathLottieSource.load(context, listener)
                }

                override fun onFailed() {
                    listener?.onError(null)
                }
            })
        } catch (throwable: Throwable) {
            listener?.onError(throwable)
        }
    }
}
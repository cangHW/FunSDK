package com.proxy.service.imageloader.info.pag.request.source

import android.content.Context
import android.text.TextUtils
import com.airbnb.lottie.network.FileExtension
import com.proxy.service.imageloader.info.pag.net.NetFactory
import com.proxy.service.imageloader.info.pag.net.RequestCallback
import com.proxy.service.imageloader.info.utils.StringUtils
import org.libpag.PAGFile

/**
 * @author: cangHX
 * @data: 2025/10/10 16:55
 * @desc:
 */
class UrlPagSource(
    private val url: String,
    private val cacheKey: String?
) : BasePagSourceData() {

    companion object {
        private const val CACHE_KEY_POSTFIX = "pag_cache_"
    }

    override fun load(context: Context?, listener: IPagLoadCallback?) {
        val key = if (TextUtils.isEmpty(cacheKey)) {
            "${CACHE_KEY_POSTFIX}${StringUtils.urlToString(url)}"
        } else {
            "${CACHE_KEY_POSTFIX}$cacheKey"
        }
        try {
            NetFactory.enqueue(key, url, object : RequestCallback {
                override fun onSuccess(path: String) {
                    try {
                        val file = PAGFile.Load(path)
                        if (file == null) {
                            listener?.onError(null)
                            return
                        }
                        listener?.onResult(file)
                    } catch (throwable: Throwable) {
                        listener?.onError(throwable)
                    }
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
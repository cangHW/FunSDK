package com.proxy.service.imageloader.info.pag.option.config.source.font

import android.text.TextUtils
import com.proxy.service.imageloader.info.pag.net.NetFactory
import com.proxy.service.imageloader.info.pag.net.RequestCallback
import com.proxy.service.imageloader.info.pag.option.config.RequestFailedException
import com.proxy.service.imageloader.info.utils.StringUtils
import org.libpag.PAGFont
import org.libpag.PAGText

/**
 * @author: cangHX
 * @data: 2025/10/16 10:54
 * @desc:
 */
class FontUrlSource(
    private val url: String,
    private val cacheKey: String?,
    isFontStyleEnable: Boolean
) : BaseFontSource(isFontStyleEnable) {

    companion object {
        private const val CACHE_KEY_POSTFIX = "ttf_cache_"
    }

    private val lock = Any()

    @Volatile
    private var path: String? = null

    @Volatile
    private var exception: Throwable? = null

    @Volatile
    private var pagText: PAGText? = null

    @Volatile
    private var callback: IFontLoadCallback? = null


    override fun preLoad() {
        if (TextUtils.isEmpty(url)) {
            exception = IllegalArgumentException("The font url cannot be empty. url=$url")
            return
        }

        val cacheKey = if (TextUtils.isEmpty(cacheKey)) {
            "$CACHE_KEY_POSTFIX${StringUtils.urlToString(url)}"
        } else {
            "$CACHE_KEY_POSTFIX$cacheKey"
        }
        try {
            NetFactory.enqueue(cacheKey, url, object : RequestCallback {
                override fun onSuccess(path: String) {
                    this@FontUrlSource.path = path
                    tryCall()
                }

                override fun onFailed() {
                    this@FontUrlSource.exception = RequestFailedException()
                    tryCall()
                }
            })
        } catch (throwable: Throwable) {
            this.exception = throwable
            tryCall()
        }
    }

    override fun loadFont(pagText: PAGText, callback: IFontLoadCallback) {
        if (TextUtils.isEmpty(url)) {
            callback.onError(IllegalArgumentException("The font url cannot be empty. url=$url"))
            return
        }

        this.pagText = pagText
        this.callback = callback
        tryCall()
    }

    private fun tryCall() {
        synchronized(lock) {
            val pt = pagText ?: return
            val cb = callback ?: return

            path?.let {
                try {
                    val font = PAGFont.RegisterFont(it)
                    if (font == null) {
                        cb.onError(null)
                        return
                    }
                    loadFontToPAGText(pt, font)
                    cb.onResult(pt)
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
        return "FontUrlSource(url=$url, cacheKey=$cacheKey, isFontStyleEnable=$isFontStyleEnable)"
    }
}
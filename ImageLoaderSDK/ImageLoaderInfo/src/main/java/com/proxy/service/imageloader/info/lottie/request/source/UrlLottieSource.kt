package com.proxy.service.imageloader.info.lottie.request.source

import android.content.Context
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieListener
import com.airbnb.lottie.LottieTask

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class UrlLottieSource(
    private val url: String,
    private val key: String?
) : BaseLottieSourceData() {

    override fun load(context: Context?, listener: ILottieLoadCallback?) {
        var lottieTask: LottieTask<LottieComposition>? = null
        var successListener: LottieListener<LottieComposition>? = null
        var errorListener: LottieListener<Throwable>? = null

        try {
            lottieTask = if (key.isNullOrEmpty() || key.isBlank()) {
                LottieCompositionFactory.fromUrl(context, url)
            } else {
                LottieCompositionFactory.fromUrl(context, url, key)
            }
            successListener = LottieListener<LottieComposition> { result ->
                removeLottieListener(lottieTask, successListener, errorListener)
                if (result == null){
                    listener?.onError(null)
                    return@LottieListener
                }
                listener?.onResult(result)
            }

            errorListener = LottieListener<Throwable> { result ->
                removeLottieListener(lottieTask, successListener, errorListener)
                listener?.onError(result)
            }

            lottieTask.addListener(successListener)
            lottieTask.addFailureListener(errorListener)
        } catch (throwable: Throwable) {
            listener?.onError(throwable)
            removeLottieListener(lottieTask, successListener, errorListener)
        }
    }

    private fun removeLottieListener(
        lottieTask: LottieTask<LottieComposition>?,
        successListener: LottieListener<LottieComposition>?,
        errorListener: LottieListener<Throwable>?
    ) {
        lottieTask?.removeListener(successListener)
        lottieTask?.removeFailureListener(errorListener)
    }

    override fun hashCode(): Int {
        return (url + key).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is UrlLottieSource) {
            return url == other.url && key == other.key
        }
        return false
    }
}
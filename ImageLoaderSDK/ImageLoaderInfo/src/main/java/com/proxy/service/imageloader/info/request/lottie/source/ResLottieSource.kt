package com.proxy.service.imageloader.info.request.lottie.source

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieListener
import com.airbnb.lottie.LottieTask
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.info.config.Config
import com.proxy.service.imageloader.info.loader.lottie.ILottieLoadCallback

/**
 * @author: cangHX
 * @data: 2024/5/16 11:38
 * @desc:
 */
class ResLottieSource(@RawRes @DrawableRes private val resourceId: Int) : BaseLottieSourceData() {

    override fun load(context: Context?, listener: ILottieLoadCallback?) {
        var lottieTask: LottieTask<LottieComposition>? = null
        var successListener: LottieListener<LottieComposition>? = null
        var errorListener: LottieListener<Throwable>? = null

        try {
            lottieTask = LottieCompositionFactory.fromRawRes(context, resourceId)
            successListener = LottieListener<LottieComposition> { result ->
                removeLottieListener(lottieTask, successListener, errorListener)
                listener?.onResult(result)
            }

            errorListener = LottieListener<Throwable> { result ->
                removeLottieListener(lottieTask, successListener, errorListener)
                listener?.onError(result)
            }

            lottieTask.addListener(successListener)
            lottieTask.addFailureListener(errorListener)
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).d(throwable)
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
        return "$resourceId".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is ResLottieSource) {
            return resourceId == other.resourceId
        }
        return false
    }
}
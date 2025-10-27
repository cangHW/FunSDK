package com.proxy.service.imageloader.info.lottie.request.source

import android.content.Context
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieListener
import com.airbnb.lottie.LottieTask
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import java.io.FileInputStream
import java.util.zip.ZipInputStream

/**
 * @author: cangHX
 * @data: 2024/5/16 11:36
 * @desc:
 */
class PathLottieSource(
    private val path: String
) : BaseLottieSourceData() {

    override fun load(context: Context?, listener: ILottieLoadCallback?) {
        var lottieTask: LottieTask<LottieComposition>? = null
        var successListener: LottieListener<LottieComposition>? = null
        var errorListener: LottieListener<Throwable>? = null

        try {
            lottieTask = if (path.endsWith(ImageLoaderConstants.LOTTIE_TYPE_JSON)) {
                LottieCompositionFactory.fromJsonInputStream(FileInputStream(path), path)
            } else if (path.endsWith(ImageLoaderConstants.LOTTIE_TYPE_ZIP)) {
                LottieCompositionFactory.fromZipStream(ZipInputStream(FileInputStream(path)), path)
            } else {
                null
            }

            if (lottieTask == null) {
                listener?.onError(IllegalArgumentException("The file path format is abnormal. path=$path"))
                return
            }

            successListener = LottieListener<LottieComposition> { result ->
                removeLottieListener(lottieTask, successListener, errorListener)
                if (result == null) {
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
}
package com.proxy.service.imageloader.info.pag.info

import android.view.ViewGroup
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationUpdateCallback
import com.proxy.service.imageloader.info.R
import com.proxy.service.imageloader.info.base.BaseInfo
import com.proxy.service.imageloader.info.pag.loader.controller.PagControllerImpl
import com.proxy.service.imageloader.info.pag.loader.view.PagViewImpl
import com.proxy.service.imageloader.info.pag.option.config.BaseConfig
import com.proxy.service.imageloader.info.pag.request.source.BasePagSourceData
import com.proxy.service.imageloader.info.pag.request.source.BasePagSourceData.IPagLoadCallback
import org.libpag.PAGFile

/**
 * @author: cangHX
 * @data: 2025/10/10 15:09
 * @desc:
 */
class PagInfo : BaseInfo() {

    var sourceData: BasePagSourceData? = null
    val configData = ArrayList<BaseConfig>()

    var isAutoPlay: Boolean = ImageLoaderConstants.IS_AUTO_PLAY
    var loopCount: Int? = null
    var progress: Double = 0.0

    var width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    private val pagViewListener = PagViewListenerImpl(this)

    var loadErrorCallback: LoadErrorCallback? = null
    var animationStartCallback: PagAnimationCallback? = null
    var animationEndCallback: PagAnimationCallback? = null
    var animationCancelCallback: PagAnimationCallback? = null
    var animationRepeatCallback: PagAnimationCallback? = null
    var animationUpdateCallback: PagAnimationUpdateCallback? = null


    fun loadConfig(view: PagViewImpl, controller: PagControllerImpl) {
        if (sourceData == null) {
            loadErrorCallback?.onLoadError()
            return
        }

        checkListenerForView(view)

        sourceData?.load(getContext(), object : IPagLoadCallback {
            override fun onResult(result: PAGFile) {
                loadResourceConfig(ArrayList(configData), view, result) {
                    view.composition = result
                    setConfig(controller)
                }
            }

            override fun onError(result: Throwable?) {
                callLoadError(result)
            }
        })
    }


    private fun checkListenerForView(view: PagViewImpl) {
        val source = view.getTag(R.id.cs_imageload_info_pag_view_tag) as? PagInfo?
        view.setTag(R.id.cs_imageload_info_pag_view_tag, this)

        if (source != null) {
            try {
                view.removeListener(source.pagViewListener)
            } catch (_: Throwable) {
            }
        }
        try {
            view.addListener(pagViewListener)
        } catch (throwable: Throwable) {
            CsLogger.tag(ImageLoaderConstants.TAG).e(throwable)
        }
    }

    private fun loadResourceConfig(
        configs: ArrayList<BaseConfig>,
        view: PagViewImpl,
        result: PAGFile,
        complete: () -> Unit
    ) {
        val cf = configs.removeFirstOrNull()
        if (cf == null) {
            complete()
            return
        }
        cf.load(result, object : BaseConfig.IConfigLoadCallback {
            override fun onResult(result: PAGFile) {
                loadResourceConfig(configs, view, result, complete)
            }

            override fun onError(result: Throwable?) {
                callLoadError(result)
            }
        })
    }

    private fun setConfig(controller: PagControllerImpl) {
        loopCount?.let {
            controller.setLoopCount(it)
        }
        controller.setAnimationProgress(progress)
        if (isAutoPlay) {
            controller.playAnimation()
        }
    }

    private fun callLoadError(result: Throwable?) {
        result?.let {
            CsLogger.tag(ImageLoaderConstants.TAG).e(it)
        }
        loadErrorCallback?.onLoadError()
    }
}
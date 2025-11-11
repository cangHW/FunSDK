package com.proxy.service.imageloader.info.pag.info

import android.view.ViewGroup
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationUpdateCallback
import com.proxy.service.imageloader.base.option.pag.scene.PagSceneMode
import com.proxy.service.imageloader.info.base.BaseInfo
import com.proxy.service.imageloader.info.pag.loader.controller.PagControllerImpl
import com.proxy.service.imageloader.info.pag.loader.view.IView
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

    var sceneMode: PagSceneMode = ImageLoaderConstants.DEFAULT_PAG_SCENE_MODE
    var isAutoPlay: Boolean = ImageLoaderConstants.IS_AUTO_PLAY
    var loopCount: Int? = null
    var progress: Double = 0.0

    var width: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    var loadErrorCallback: LoadErrorCallback? = null
    var animationStartCallback: PagAnimationCallback? = null
    var animationEndCallback: PagAnimationCallback? = null
    var animationCancelCallback: PagAnimationCallback? = null
    var animationRepeatCallback: PagAnimationCallback? = null
    var animationUpdateCallback: PagAnimationUpdateCallback? = null


    fun loadConfig(view: IView, controller: PagControllerImpl) {
        if (sourceData == null) {
            loadErrorCallback?.onLoadError()
            return
        }

        view.checkListener(this)

        sourceData?.load(getContext(), object : IPagLoadCallback {
            override fun onResult(result: PAGFile) {
                loadResourceConfig(ArrayList(configData), result) {
                    view.setPagComposition(result)
                    setConfig(controller)
                }
            }

            override fun onError(result: Throwable?) {
                callLoadError(result)
            }
        })
    }

    private fun loadResourceConfig(
        configs: ArrayList<BaseConfig>,
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
                loadResourceConfig(configs, result, complete)
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
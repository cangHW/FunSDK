package com.proxy.service.imageloader.info.pag.option

import com.proxy.service.imageloader.base.option.base.LoadErrorCallback
import com.proxy.service.imageloader.base.option.pag.IPageOption
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationUpdateCallback
import com.proxy.service.imageloader.base.option.pag.image.PagImageData
import com.proxy.service.imageloader.base.option.pag.scene.PagSceneMode
import com.proxy.service.imageloader.base.option.pag.txt.PagTextData
import com.proxy.service.imageloader.info.pag.info.PagInfo
import com.proxy.service.imageloader.info.pag.loader.PagLoaderImpl
import com.proxy.service.imageloader.info.pag.option.config.ImageConfig
import com.proxy.service.imageloader.info.pag.option.config.TextConfig

/**
 * @author: cangHX
 * @data: 2025/10/10 17:07
 * @desc:
 */
class PagOptionImpl(
    private val info: PagInfo
) : PagLoaderImpl(info), IPageOption {

    override fun setLoadErrorCallback(callback: LoadErrorCallback): IPageOption {
        info.loadErrorCallback = callback
        return this
    }

    override fun setPagSceneMode(mode: PagSceneMode): IPageOption {
        info.sceneMode = mode
        return this
    }

    override fun setAutoPlay(isAutoPlay: Boolean): IPageOption {
        info.isAutoPlay = isAutoPlay
        return this
    }

    override fun setLoopCount(count: Int): IPageOption {
        info.loopCount = count
        return this
    }

    override fun setAnimationProgress(progress: Double): IPageOption {
        info.progress = progress
        return this
    }

    override fun replaceText(index: Int, data: PagTextData): IPageOption {
        val config = TextConfig.create(index, data)
        config.tryPreLoad()
        info.configData.add(config)
        return this
    }

    override fun replaceImage(index: Int, data: PagImageData): IPageOption {
        val config = ImageConfig.create(index, data)
        config.tryPreLoad()
        info.configData.add(config)
        return this
    }

    override fun replaceImageByName(name: String, data: PagImageData): IPageOption {
        val config = ImageConfig.create(name, data)
        config.tryPreLoad()
        info.configData.add(config)
        return this
    }

    override fun setAnimationStartCallback(callback: PagAnimationCallback): IPageOption {
        info.animationStartCallback = callback
        return this
    }

    override fun setAnimationEndCallback(callback: PagAnimationCallback): IPageOption {
        info.animationEndCallback = callback
        return this
    }

    override fun setAnimationCancelCallback(callback: PagAnimationCallback): IPageOption {
        info.animationCancelCallback = callback
        return this
    }

    override fun setAnimationRepeatCallback(callback: PagAnimationCallback): IPageOption {
        info.animationRepeatCallback = callback
        return this
    }

    override fun setAnimationUpdateCallback(callback: PagAnimationUpdateCallback): IPageOption {
        info.animationUpdateCallback = callback
        return this
    }

}
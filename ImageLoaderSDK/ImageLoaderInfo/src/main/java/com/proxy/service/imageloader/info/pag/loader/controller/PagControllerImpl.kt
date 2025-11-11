package com.proxy.service.imageloader.info.pag.loader.controller

import com.proxy.service.imageloader.base.loader.pag.PagController
import com.proxy.service.imageloader.info.pag.loader.view.IView

/**
 * @author: cangHX
 * @data: 2025/10/10 18:40
 * @desc:
 */
class PagControllerImpl(
    private val view: IView
) : PagController {

    override fun setAnimationProgress(progress: Double) {
        view.setPagProgress(progress)
    }

    override fun setLoopCount(count: Int) {
        view.setPagRepeatCount(count)
    }

    override fun playAnimation() {
        view.playPag()
    }

    override fun cancelAnimation() {
        view.stopPag()
    }
}
package com.proxy.service.imageloader.info.pag.loader.controller

import com.proxy.service.imageloader.base.loader.pag.PagController
import com.proxy.service.imageloader.info.pag.info.PagInfo
import org.libpag.PAGView

/**
 * @author: cangHX
 * @data: 2025/10/10 18:40
 * @desc:
 */
class PagControllerImpl(
    private val view: PAGView
) : PagController {

    override fun setAnimationProgress(progress: Double) {
        view.progress = progress
    }

    override fun setLoopCount(count: Int) {
        view.setRepeatCount(count)
    }

    override fun playAnimation() {
        view.play()
    }

    override fun cancelAnimation() {
        view.stop()
    }
}
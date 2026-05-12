package com.proxy.service.imageloader.info.pag.loader.view

import com.proxy.service.imageloader.info.pag.info.PagInfo
import org.libpag.PAGComposition

/**
 * @author: cangHX
 * @date: 2025/11/11 16:00
 * @desc:
 */
interface IView {

    fun checkListener(pagInfo: PagInfo)

    fun setPagComposition(composition: PAGComposition?)

    fun setPagProgress(progress: Double)

    fun setPagRepeatCount(count: Int)

    fun playPag()

    fun stopPag()

    fun releaseCache()
}
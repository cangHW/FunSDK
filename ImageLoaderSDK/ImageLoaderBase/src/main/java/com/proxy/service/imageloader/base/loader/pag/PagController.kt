package com.proxy.service.imageloader.base.loader.pag

import androidx.annotation.FloatRange
import com.proxy.service.imageloader.base.option.pag.IPagAction

/**
 * @author: cangHX
 * @data: 2025/10/10 17:16
 * @desc:
 */
interface PagController: IPagAction<Unit> {

    /**
     * 播放动画
     * */
    fun playAnimation()

    /**
     * 取消动画
     * */
    fun cancelAnimation()

}
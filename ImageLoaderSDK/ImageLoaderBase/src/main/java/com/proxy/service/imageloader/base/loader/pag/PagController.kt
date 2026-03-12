package com.proxy.service.imageloader.base.loader.pag

import com.proxy.service.imageloader.base.option.pag.IPagAction

/**
 * @author: cangHX
 * @data: 2025/10/10 17:16
 * @desc:
 */
interface PagController : IPagAction<Unit> {

    /**
     * 播放动画
     * */
    fun playAnimation()

    /**
     * 取消动画
     * */
    fun cancelAnimation()

    /**
     * 主动释放资源, 默认会在 view DetachedFromWindow 时释放
     * */
    fun freeCache()
}
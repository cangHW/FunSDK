package com.proxy.service.imageloader.base.option.pag

import androidx.annotation.FloatRange

/**
 * @author: cangHX
 * @data: 2025/10/20 11:36
 * @desc:
 */
interface IPagAction<T> {

    /**
     * 设置播放次数
     *
     * @param count 播放次数, 如果小于 0 则无限循环播放, 等于 0 则使用资源自身默认的循环次数
     * */
    fun setLoopCount(count: Int): T

    /**
     * 设置动画初始进度
     * */
    fun setAnimationProgress(@FloatRange(from = 0.0, to = 1.0) progress: Double): T
}
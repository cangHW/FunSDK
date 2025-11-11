package com.proxy.service.imageloader.base.option.pag.scene

/**
 * @author: cangHX
 * @data: 2025/11/11 15:42
 * @desc:
 */
enum class PagSceneMode {

    /**
     * 高渲染性能, 适合复杂动画
     * */
    QUALITY,

    /**
     * 低内存占用，轻量化, 适合简单动效
     * */
    PERFORMANCE;
}
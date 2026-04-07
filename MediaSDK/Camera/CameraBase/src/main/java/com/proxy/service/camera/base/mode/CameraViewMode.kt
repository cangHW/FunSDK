package com.proxy.service.camera.base.mode

/**
 * @author: cangHX
 * @data: 2026/2/6 14:09
 * @desc:
 */
enum class CameraViewMode {

    /**
     * 性能稍逊，适合需要与 UI 交互的场景。
     * */
    TEXTURE_VIEW,

    /**
     * 高性能，适合高帧率场景。
     * */
    SURFACE_VIEW;

}
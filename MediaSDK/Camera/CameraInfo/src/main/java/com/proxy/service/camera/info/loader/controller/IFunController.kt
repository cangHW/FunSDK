package com.proxy.service.camera.info.loader.controller

import android.view.Surface
import com.proxy.service.camera.base.mode.loader.CameraMeteringMode
import com.proxy.service.camera.base.mode.loader.CameraFaceMode

/**
 * @author: cangHX
 * @data: 2026/3/24 14:11
 * @desc:
 */
interface IFunController {

    interface SurfaceChangedCallback {
        fun onSurfaceChanged()

        fun refreshPreview(
            templateType: Int,
            tempSurfaces: List<Surface>,
            success: (() -> Unit),
            failed: (() -> Unit)
        )
    }

    interface IParamsController {
        fun getCameraFaceModeFromFunController(): CameraFaceMode?

        fun getCameraMeteringModeFromFunController(): CameraMeteringMode?
    }

    /**
     * 获取 surface 窗口
     * */
    fun getSurface(): Surface?

    /**
     * 设置窗口变化监听
     * */
    fun setSurfaceChangedCallback(callback: SurfaceChangedCallback)

    /**
     * 设置参数控制器
     * */
    fun setParamsController(controller: IParamsController)

    /**
     * 打断
     * */
    fun abort()

    /**
     * 销毁
     * */
    fun destroy()

}
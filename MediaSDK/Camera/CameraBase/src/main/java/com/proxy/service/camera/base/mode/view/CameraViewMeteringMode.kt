package com.proxy.service.camera.base.mode.view

import com.proxy.service.camera.base.mode.loader.CameraMeteringMode
import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2026/2/7 15:48
 * @desc:
 */
sealed class CameraViewMeteringMode private constructor() {

    companion object {
        private val size = CsDpUtils.dp2px(80f)
    }

    /**
     * 自动模式
     * */
    object AutoMode : CameraViewMeteringMode() {
        override fun toCameraMode(): CameraMeteringMode {
            return CameraMeteringMode.AutoMode
        }

        override fun toString(): String {
            return "AutoMode()"
        }
    }

    /**
     * 触摸模式
     *
     * @param width     区域大小
     * @param height    区域大小
     * */
    class TouchMode(val width: Int = size, val height: Int = size) : CameraViewMeteringMode() {
        override fun toCameraMode(): CameraMeteringMode {
            return CameraMeteringMode.AutoMode
        }

        override fun toString(): String {
            return "TouchMode(width=$width, height=$height)"
        }
    }

    /**
     * 转化当前模式
     * */
    abstract fun toCameraMode(): CameraMeteringMode
}
package com.proxy.service.camera.base.mode

import com.proxy.service.camera.base.mode.af.FocusAreaInfo
import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2026/2/7 15:48
 * @desc:
 */
sealed class CameraViewAfMode private constructor() {

    companion object {
        private val dp40 = CsDpUtils.dp2px(40f)
    }

    /**
     * 自动对焦模式
     * */
    object AfAutoMode : CameraViewAfMode() {
        override fun toCameraAfMode(): CameraAfMode {
            return CameraAfMode.AfAutoMode
        }

        override fun toString(): String {
            return "AfAutoMode()"
        }
    }

    /**
     * 固定对焦区域模式
     * */
    class AfFixedMode(val list: List<FocusAreaInfo>) : CameraViewAfMode() {
        override fun toCameraAfMode(): CameraAfMode {
            return CameraAfMode.AfFixedMode(list)
        }

        override fun toString(): String {
            return "AfFixedMode(list=$list)"
        }
    }

    /**
     * 触摸对焦模式
     *
     * @param width     对焦区域大小
     * @param height    对焦区域大小
     * */
    class AfTouchMode(val width: Int = dp40, val height: Int = dp40) : CameraViewAfMode() {
        override fun toCameraAfMode(): CameraAfMode {
            return CameraAfMode.AfAutoMode
        }

        override fun toString(): String {
            return "AfTouchMode(width=$width, height=$height)"
        }
    }

    /**
     * 转化当前模式
     * */
    abstract fun toCameraAfMode(): CameraAfMode
}
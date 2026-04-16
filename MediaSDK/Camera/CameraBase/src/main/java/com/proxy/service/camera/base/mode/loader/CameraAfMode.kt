package com.proxy.service.camera.base.mode.loader

import com.proxy.service.camera.base.mode.loader.af.FocusAreaInfo

/**
 * @author: cangHX
 * @data: 2026/2/7 15:48
 * @desc:
 */
sealed class CameraAfMode private constructor() {

    /**
     * 自动对焦模式
     * */
    object AfAutoMode : CameraAfMode(){
        override fun toString(): String {
            return "AfAutoMode()"
        }
    }

    /**
     * 固定对焦区域模式
     * */
    class AfFixedMode(val list: List<FocusAreaInfo>) : CameraAfMode(){
        override fun toString(): String {
            return "AfFixedMode(list=$list)"
        }
    }

}
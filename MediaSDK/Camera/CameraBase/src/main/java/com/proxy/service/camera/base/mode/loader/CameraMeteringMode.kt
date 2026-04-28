package com.proxy.service.camera.base.mode.loader

import com.proxy.service.camera.base.mode.loader.bean.MeteringAreaInfo

/**
 * @author: cangHX
 * @data: 2026/2/7 15:48
 * @desc: 相机测光模式。
 *
 *  用于统一配置 AF（对焦）、AE（曝光）的方式。
 */
sealed class CameraMeteringMode private constructor() {

    /**
     * 自动模式
     * */
    object AutoMode : CameraMeteringMode(){
        override fun toString(): String {
            return "AutoMode()"
        }
    }

    /**
     * 固定区域模式, 区域坐标使用 Camera2 传感器 active array 坐标系。
     * */
    class FixedMode(
        val areas: List<MeteringAreaInfo>
    ) : CameraMeteringMode(){
        override fun toString(): String {
            return "FixedMode(areas=$areas)"
        }
    }

}
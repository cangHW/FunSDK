package com.proxy.service.core.framework.system.screen.enums

/**
 * @author: cangHX
 * @data: 2025/5/24 18:57
 * @desc:
 */
enum class OrientationEnum {

    /**
     * 未设置
     * */
    UNDEFINED,

    /**
     * 竖屏
     * */
    PORTRAIT,

    /**
     * 横屏
     * */
    LANDSCAPE;

    companion object {
        fun valueOf(rotation: RotationEnum?): OrientationEnum {
            if (rotation == RotationEnum.ROTATION_0) {
                return PORTRAIT
            }
            if (rotation == RotationEnum.ROTATION_180) {
                return PORTRAIT
            }
            if (rotation == RotationEnum.ROTATION_90) {
                return LANDSCAPE
            }
            if (rotation == RotationEnum.ROTATION_270) {
                return LANDSCAPE
            }
            return UNDEFINED
        }

        fun valueOf(degree: Int?): OrientationEnum {
            return valueOf(RotationEnum.valueOf(degree))
        }
    }

}
package com.proxy.service.core.framework.system.screen.enums

import android.view.Surface

/**
 * @author: cangHX
 * @data: 2025/5/24 17:46
 * @desc: 顺时针旋转度数
 */
enum class RotationEnum(val degree: Int) {

    ROTATION_0(Surface.ROTATION_0),

    ROTATION_90(Surface.ROTATION_90),

    ROTATION_180(Surface.ROTATION_180),

    ROTATION_270(Surface.ROTATION_270);

    companion object {

        fun valueOf(degree: Int?): RotationEnum? {
            when (degree) {
                ROTATION_0.degree -> {
                    return ROTATION_0
                }

                ROTATION_90.degree -> {
                    return ROTATION_90
                }

                ROTATION_180.degree -> {
                    return ROTATION_180
                }

                ROTATION_270.degree -> {
                    return ROTATION_270
                }
            }
            return null
        }

    }

}
package com.proxy.service.camera.base.mode.loader

/**
 * @author: cangHX
 * @data: 2026/2/5 17:17
 * @desc:
 */
enum class SensorOrientationMode(val degree: Int) {

    ORIENTATION_0(0),

    ORIENTATION_90(90),

    ORIENTATION_180(180),

    ORIENTATION_270(270);


    companion object {
        fun valueOf(degree: Int?): SensorOrientationMode? {
            when (degree) {
                ORIENTATION_0.degree -> {
                    return ORIENTATION_0
                }

                ORIENTATION_90.degree -> {
                    return ORIENTATION_90
                }

                ORIENTATION_180.degree -> {
                    return ORIENTATION_180
                }

                ORIENTATION_270.degree -> {
                    return ORIENTATION_270
                }
            }
            return null
        }
    }

}
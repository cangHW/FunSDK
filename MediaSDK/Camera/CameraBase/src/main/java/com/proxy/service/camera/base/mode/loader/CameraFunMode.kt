package com.proxy.service.camera.base.mode.loader

import com.proxy.service.camera.base.R

/**
 * @author: cangHX
 * @data: 2026/2/7 13:55
 * @desc:
 */
enum class CameraFunMode(
    private val modeName: String,
    private val modeRes: Int
) {

    /**
     * 拍照
     * */
    CAPTURE("照片", R.drawable.cs_camera_base_capture_photo),

    /**
     * 录像
     * */
    RECORD("录像", 0);

    /**
     * 获取模式名称
     * */
    fun getModeName(): String {
        return modeName
    }

    /**
     * 获取模式图标
     * */
    fun getModeRes(): Int {
        return modeRes
    }

    companion object {
        fun getAll(): List<CameraFunMode> {
            val list = ArrayList<CameraFunMode>()
            list.add(CAPTURE)
            list.add(RECORD)
            return list
        }
    }

}
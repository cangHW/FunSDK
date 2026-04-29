package com.proxy.service.camera.base.mode.loader

import com.proxy.service.camera.base.R
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2026/2/7 13:55
 * @desc:
 */
enum class CameraFunMode(
    private val modeNameRes: Int
) {

    /**
     * 拍照
     * */
    CAPTURE(R.string.cs_camera_base_capture),

    /**
     * 录像
     * */
    RECORD(R.string.cs_camera_base_record);

    /**
     * 获取模式名称
     * */
    fun getModeName(): String {
        return CsContextManager.getApplication().getString(modeNameRes)
    }

    override fun toString(): String {
        return "CameraFunMode(modeName='${getModeName()}')"
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
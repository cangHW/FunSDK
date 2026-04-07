package com.proxy.service.camera.base.mode

import com.proxy.service.core.service.media.CsMediaCamera

/**
 * @author: cangHX
 * @data: 2026/2/5 10:48
 * @desc:
 */
sealed class CameraFaceMode {

    /**
     * 后置摄像头
     * */
    object FaceBack : CameraFaceMode() {

        override fun getCameraId(): String? {
            return CsMediaCamera.getCameraFaceBackId()
        }

        override fun getCameraDesc(): String {
            return "后置摄像头"
        }
    }

    /**
     * 前置摄像头
     * */
    object FaceFront : CameraFaceMode() {

        override fun getCameraId(): String? {
            return CsMediaCamera.getCameraFaceFrontId()
        }

        override fun getCameraDesc(): String {
            return "前置摄像头"
        }
    }

    /**
     * 自定义相机
     */
    abstract class Custom : CameraFaceMode()


    /**
     * 摄像头 id
     * */
    abstract fun getCameraId(): String?

    /**
     * 摄像头说明
     * */
    abstract fun getCameraDesc(): String
}
package com.proxy.service.camera.base.view

import com.proxy.service.camera.base.base.BaseCamera
import com.proxy.service.camera.base.callback.TakePictureCallback
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode

/**
 * @author: cangHX
 * @data: 2026/2/5 10:26
 * @desc:
 */
interface ICameraView : BaseCamera {

    /**
     * 设置预览尺寸, 针对当前相机模式与方向有效
     * */
    fun setPreviewSize(width: Int, height: Int)

    /**
     * 设置相机模式
     * */
    fun setCameraMode(mode: CameraMode)

    /**
     * 设置相机对焦模式
     * */
    fun setCameraViewAfMode(mode: CameraViewAfMode)

    /**
     * 设置拍照图片尺寸, 针对当前相机模式与方向有效
     * */
    fun setPictureCaptureSize(width: Int, height: Int)

    /**
     * 拍照
     * */
    fun startPictureCapture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?)

}
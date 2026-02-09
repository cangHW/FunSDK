package com.proxy.service.camera.base.page

import android.content.Context
import com.proxy.service.camera.base.callback.PageTakePictureCallback

/**
 * @author: cangHX
 * @data: 2026/2/4 16:05
 * @desc:
 */
interface ICameraPageLoader {

    /**
     * 设置拍照回调
     * */
    fun setCapturePhotoCallback(callback: PageTakePictureCallback): ICameraPageLoader

    /**
     * 启动
     * */
    fun launch()

    /**
     * 启动
     * */
    fun launch(context: Context)

}
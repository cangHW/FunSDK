package com.proxy.service.camera.base.callback.loader

import android.media.Image

/**
 * @author: cangHX
 * @data: 2026/3/26 15:15
 * @desc:
 */
interface PreviewCallback {

    /**
     * 预览数据回调
     * */
    fun onPreviewChanged(image: Image)

}
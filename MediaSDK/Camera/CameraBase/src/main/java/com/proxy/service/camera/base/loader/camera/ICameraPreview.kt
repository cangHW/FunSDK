package com.proxy.service.camera.base.loader.camera

import android.view.Surface
import com.proxy.service.camera.base.callback.loader.PreviewCallback
import com.proxy.service.camera.base.mode.PreviewImageFormatMode

/**
 * @author: cangHX
 * @data: 2026/3/23 18:13
 * @desc:
 */
interface ICameraPreview : ICameraAction {

    /**
     * 设置预览回调
     *
     * @param format    预览数据图像格式
     * @param pWidth    回调的预览图宽度
     * @param pHeight   回调的预览图高度
     * */
    fun setPreviewCallback(
        format: PreviewImageFormatMode,
        pWidth: Int,
        pHeight: Int,
        callback: PreviewCallback
    )

    /**
     * 清空并重新设置预览窗口
     * */
    fun setPreviewSurface(surface: Surface)

    /**
     * 添加预览窗口
     * */
    fun addPreviewSurface(surface: Surface)

}
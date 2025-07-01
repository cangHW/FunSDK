package com.proxy.service.document.image.base.mode.builder

import com.proxy.service.document.image.base.mode.CropMode

/**
 * @author: cangHX
 * @data: 2025/6/26 09:49
 * @desc:
 */
interface ICropFrameMoveModeBuilder {

    /**
     * 创建模式
     * */
    fun build(): CropMode

}
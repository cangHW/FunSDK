package com.proxy.service.imageloader.base.option.pag.image

/**
 * @author: cangHX
 * @data: 2025/10/16 17:45
 * @desc:
 */
enum class PagScaleMode {

    /**
     * 默认, 以目标区域左上角为原点正常显示图像, 可能留有空白或裁剪多余图像
     * */
    NONE,

    /**
     * 拉伸图像以完全填充目标区域，可能导致图像变形
     * */
    FIT_XY,

    /**
     * 保持宽高比，缩放图像完整显示在目标区域，可能留有空白
     * */
    CENTER_INSIDE,

    /**
     * 保持宽高比，缩放图像以局中填满目标区域，裁剪多余图像
     * */
    CENTER_CROP;

}
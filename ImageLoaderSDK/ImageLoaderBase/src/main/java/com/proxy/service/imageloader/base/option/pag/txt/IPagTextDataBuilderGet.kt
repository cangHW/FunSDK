package com.proxy.service.imageloader.base.option.pag.txt

/**
 * @author: cangHX
 * @data: 2025/10/15 22:42
 * @desc:
 */
interface IPagTextDataBuilderGet {

    /**
     * 获取文字
     * */
    fun getText(): String

    /**
     * 获取字体信息
     * */
    fun getPagTextFont(): PagTextFont?

    /**
     * 获取字体大小
     * */
    fun getPagTextSize(): Float?

}
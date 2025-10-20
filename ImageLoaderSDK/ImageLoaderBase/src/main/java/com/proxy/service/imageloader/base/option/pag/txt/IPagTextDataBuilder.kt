package com.proxy.service.imageloader.base.option.pag.txt

/**
 * @author: cangHX
 * @data: 2025/10/15 22:42
 * @desc:
 */
interface IPagTextDataBuilder {

    /**
     * 设置字体信息
     * */
    fun setPagTextFont(font: PagTextFont): IPagTextDataBuilder

    /**
     * 设置字体大小
     * */
    fun setPagTextSizePx(size: Float): IPagTextDataBuilder

    /**
     * 设置字体大小
     * */
    fun setPagTextSizeSp(size: Float): IPagTextDataBuilder

    /**
     * 设置字体大小
     * */
    fun setPagTextSizeDp(size: Float): IPagTextDataBuilder

    /**
     * 创建一个配置
     * */
    fun build(): PagTextData
}
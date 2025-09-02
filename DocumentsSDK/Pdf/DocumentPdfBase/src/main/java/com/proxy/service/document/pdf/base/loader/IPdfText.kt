package com.proxy.service.document.pdf.base.loader

/**
 * @author: cangHX
 * @data: 2025/8/25 11:15
 * @desc:
 */
interface IPdfText : IPdfEdit {

    /**
     * 获取页面内文字数量, 如果获取失败返回: -1
     * */
    fun getTextCount(pageIndex: Int): Int

    /**
     * 获取页面内文字, 如果获取失败返回空字串
     * */
    fun getText(pageIndex: Int): String
}
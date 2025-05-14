package com.proxy.service.document.base.pdf.loader

import com.proxy.service.document.base.pdf.info.CatalogueData
import com.proxy.service.document.base.pdf.info.LinkData
import com.proxy.service.document.base.pdf.info.MetaData
import com.proxy.service.document.base.pdf.info.PageSize

/**
 * @author: cangHX
 * @data: 2025/4/30 14:53
 * @desc:
 */
interface IPdfAction : IPdfEdit {

    /**
     * 获取页面数量
     * */
    fun getPageCount(): Int

    /**
     * 获取页面宽度
     * */
    fun getPageWidthPixel(pageIndex: Int): Int

    /**
     * 获取页面高度
     * */
    fun getPageHeightPixel(pageIndex: Int): Int

    /**
     * 获取页面尺寸信息
     * */
    fun getPageSizePixel(pageIndex: Int): PageSize

    /**
     * 获取文档说明信息
     * */
    fun getDocumentMeta(): MetaData

    /**
     * 获取文档目录信息
     * */
    fun getDocumentCatalogue(): ArrayList<CatalogueData>

    /**
     * 获取目标页面的超链接信息
     * */
    fun getPageLinks(pageIndex: Int): ArrayList<LinkData>
}
package com.proxy.service.document.pdf.base.bean

/**
 * @author: cangHX
 * @data: 2025/4/30 16:08
 * @desc: 目录信息
 */
class CatalogueData {

    var hand: Long? = null

    /**
     * 标题
     * */
    var title: String? = null

    /**
     * 对应页码
     * */
    var pageIndex: Long? = null

    /**
     * 子目录信息
     * */
    val children = ArrayList<CatalogueData>()

}
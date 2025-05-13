package com.proxy.service.document.base.pdf.info

/**
 * @author: cangHX
 * @data: 2025/4/30 16:08
 * @desc:
 */
class MetaData {

    /**
     * 标题
     * */
    var title: String? = null

    /**
     * 作者
     * */
    var author: String? = null

    /**
     * 主题或内容
     * */
    var subject: String? = null

    /**
     * 关键字
     * */
    var keywords: String? = null

    /**
     * 生成PDF文档的应用程序或工具
     * */
    var creator: String? = null

    /**
     * 生成最终PDF文件的应用程序或工具，尤其是负责PDF转换的工具或库
     * */
    var producer: String? = null

    /**
     * 创建日期
     * */
    var creationDate: String? = null

    /**
     * 修改日期
     * */
    var modDate: String? = null

    override fun toString(): String {
        return "MetaData(title=$title, author=$author, subject=$subject, keywords=$keywords, creator=$creator, producer=$producer, creationDate=$creationDate, modDate=$modDate)"
    }
}
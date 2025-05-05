package com.proxy.service.document.pdf.loader.utils

import com.proxy.service.document.pdf.core.PdfiumCore
import com.proxy.service.document.pdf.loader.info.DocumentInfo

/**
 * @author: cangHX
 * @data: 2025/5/2 08:54
 * @desc:
 */
object LoaderUtils {

    /**
     * 通过页码找到对应文档对象
     * */
    fun findDocByPageIndex(
        docs: ArrayList<DocumentInfo>,
        pageIndex: Int
    ): DocumentInfo? {
        for (doc in docs) {
            if (doc.getPageStart() <= pageIndex && pageIndex <= doc.getPageEnd()) {
                return doc
            }
        }
        return null
    }

}
package com.proxy.service.document.pdf.info.loader.impl

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.pdf.base.bean.CatalogueData
import com.proxy.service.document.pdf.base.bean.MetaData
import com.proxy.service.document.pdf.base.constants.Constants
import com.proxy.service.document.pdf.info.loader.info.DocumentInfo
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2025/4/30 15:31
 * @desc:
 */
open class BasePdf {

    companion object {
        const val TAG = "${Constants.LOG_TAG_PDF_START}Loader"

        val lock = Any()
    }

    protected val isDestroy = AtomicBoolean(false)

    /**
     * 文档
     * */
    protected val docs = ArrayList<DocumentInfo>()

    /**
     * 文档说明信息
     * */
    @Volatile
    protected var meta: MetaData? = null

    /**
     * 文档目录信息
     * */
    @Volatile
    protected var catalogue: ArrayList<CatalogueData>? = null

    protected fun isNotReady(): Boolean {
        if (isDestroy.get()) {
            CsLogger.tag(TAG).e("The document is destroy.")
            return true
        }
        if (docs.isEmpty()) {
            CsLogger.tag(TAG).e("The document does not exist.")
            return true
        }
        return false
    }

    /**
     * 通过页码找到对应文档对象
     * */
    protected fun findDocByPageIndex(pageIndex: Int): DocumentInfo? {
        synchronized(lock) {
            for (doc in docs) {
                if (doc.getPageStart() <= pageIndex && pageIndex <= doc.getPageEnd()) {
                    return doc
                }
            }
        }
        return null
    }
}
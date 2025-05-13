package com.proxy.service.document.pdf.loader.impl

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.pdf.info.CatalogueData
import com.proxy.service.document.base.pdf.info.MetaData
import com.proxy.service.document.pdf.loader.info.DocumentInfo
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

    protected fun isNotReady():Boolean{
        if (docs.isEmpty()){
            CsLogger.tag(TAG).e("The document does not exist.")
            return true
        }
        return false
    }
}
package com.proxy.service.document.pdf.base.config.info

import com.proxy.service.document.pdf.base.config.enums.LoadErrorEnum
import com.proxy.service.document.pdf.base.config.source.BaseSource

/**
 * @author: cangHX
 * @data: 2025/5/5 15:03
 * @desc:
 */
class FailedResult(val source: BaseSource, val error: LoadErrorEnum){

    override fun toString(): String {
        return "FailedResult(source=${source}, error=$error)"
    }
}
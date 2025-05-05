package com.proxy.service.document.base.config.pdf.info

import com.proxy.service.document.base.config.pdf.enums.LoadErrorEnum
import com.proxy.service.document.base.config.pdf.source.BaseSource

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
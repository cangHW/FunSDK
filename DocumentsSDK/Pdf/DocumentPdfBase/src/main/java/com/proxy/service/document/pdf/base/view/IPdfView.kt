package com.proxy.service.document.pdf.base.view

import com.proxy.service.document.pdf.base.enums.ViewShowType

/**
 * @author: cangHX
 * @data: 2025/5/14 15:59
 * @desc:
 */
interface IPdfView {

    /**
     * 修改显示模式
     * */
    fun changeShowType(type: ViewShowType)

}
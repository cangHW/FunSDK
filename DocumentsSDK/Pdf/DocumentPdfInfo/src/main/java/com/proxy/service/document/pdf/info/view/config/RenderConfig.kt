package com.proxy.service.document.pdf.info.view.config

import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.base.enums.PagePixelFormat

/**
 * @author cangHX
 * @version 1.0
 * 2025/5/18 17:22
 */
class RenderConfig {

    var viewBackgroundColor: Long = PdfConstants.DEFAULT_BG_COLOR
    var pageBackgroundColor: Long = PdfConstants.DEFAULT_BG_COLOR

    var format: PagePixelFormat = PdfConstants.DEFAULT_PAGE_PIXEL_FORMAT

}
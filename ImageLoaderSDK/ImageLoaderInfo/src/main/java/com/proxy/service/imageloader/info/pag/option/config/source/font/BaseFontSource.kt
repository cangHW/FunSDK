package com.proxy.service.imageloader.info.pag.option.config.source.font

import org.libpag.PAGFont
import org.libpag.PAGText

/**
 * @author: cangHX
 * @data: 2025/10/16 10:54
 * @desc:
 */
abstract class BaseFontSource(
    protected val isFontStyleEnable: Boolean
) {

    interface IFontLoadCallback {

        fun onResult(pagText: PAGText)

        fun onError(result: Throwable?)

    }

    protected fun loadFontToPAGText(pagText: PAGText, pagFont: PAGFont) {
        pagText.fontFamily = pagFont.fontFamily
        if (isFontStyleEnable) {
            pagText.fontStyle = pagFont.fontStyle
        }
    }

    open fun preLoad() {}

    abstract fun loadFont(pagText: PAGText, callback: IFontLoadCallback)

}
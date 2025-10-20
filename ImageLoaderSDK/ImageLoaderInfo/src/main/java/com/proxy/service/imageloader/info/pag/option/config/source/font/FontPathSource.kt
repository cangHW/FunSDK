package com.proxy.service.imageloader.info.pag.option.config.source.font

import org.libpag.PAGFont
import org.libpag.PAGText

/**
 * @author: cangHX
 * @data: 2025/10/16 10:54
 * @desc:
 */
class FontPathSource(
    private val path: String,
    isFontStyleEnable: Boolean
) : BaseFontSource(isFontStyleEnable) {

    override fun loadFont(pagText: PAGText, callback: IFontLoadCallback) {
        val font = PAGFont.RegisterFont(path)
        if (font == null) {
            callback.onError(null)
            return
        }
        loadFontToPAGText(pagText, font)
        callback.onResult(pagText)
    }

    override fun toString(): String {
        return "FontPathSource(path=$path, isFontStyleEnable=$isFontStyleEnable)"
    }
}
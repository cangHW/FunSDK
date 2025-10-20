package com.proxy.service.imageloader.info.pag.option.config.source.font

import com.proxy.service.core.framework.app.context.CsContextManager
import org.libpag.PAGFont
import org.libpag.PAGText

/**
 * @author: cangHX
 * @data: 2025/10/16 10:54
 * @desc:
 */
class FontAssetsSource(
    private val fileName: String,
    isFontStyleEnable: Boolean
) : BaseFontSource(isFontStyleEnable) {

    override fun loadFont(pagText: PAGText, callback: IFontLoadCallback) {
        val context = CsContextManager.getApplication()
        val font = PAGFont.RegisterFont(context.assets, fileName)
        if (font == null) {
            callback.onError(null)
            return
        }
        loadFontToPAGText(pagText, font)
        callback.onResult(pagText)
    }

    override fun toString(): String {
        return "FontAssetsSource(fileName=$fileName, isFontStyleEnable=$isFontStyleEnable)"
    }
}
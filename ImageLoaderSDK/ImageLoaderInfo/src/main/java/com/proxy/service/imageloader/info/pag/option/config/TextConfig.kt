package com.proxy.service.imageloader.info.pag.option.config

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.option.pag.txt.PagTextData
import com.proxy.service.imageloader.info.pag.option.config.source.font.BaseFontSource
import com.proxy.service.imageloader.info.pag.option.config.source.font.BaseFontSource.IFontLoadCallback
import com.proxy.service.imageloader.info.pag.option.config.source.font.FontAssetsSource
import com.proxy.service.imageloader.info.pag.option.config.source.font.FontPathSource
import com.proxy.service.imageloader.info.pag.option.config.source.font.FontUrlSource
import org.libpag.PAGFile
import org.libpag.PAGText

/**
 * @author: cangHX
 * @data: 2025/10/13 20:39
 * @desc:
 */
class TextConfig private constructor(
    private val index: Int,
    private val data: PagTextData
) : BaseConfig() {

    companion object {
        fun create(index: Int, data: PagTextData): TextConfig {
            return TextConfig(index, data)
        }
    }

    private var fontSource: BaseFontSource = FontEmptySource()

    override fun tryPreLoad() {
        try {
            val font = data.getPagTextFont() ?: return
            val isFontStyleEnable = font.isFontStyleEnable()

            val url = data.getPagTextFont()?.getFontFamilyUrl()
            if (!url.isNullOrEmpty() && url.isNotBlank()) {
                fontSource = FontUrlSource(url, font.getFontFamilyUrlCacheKey(), isFontStyleEnable)
                return
            }
            val assetsName = data.getPagTextFont()?.getFontFamilyAssetsName()
            if (!assetsName.isNullOrEmpty() && assetsName.isNotBlank()) {
                fontSource = FontAssetsSource(assetsName, isFontStyleEnable)
                return
            }
            val path = data.getPagTextFont()?.getFontFamilyPath()
            if (!path.isNullOrEmpty() && path.isNotBlank()) {
                fontSource = FontPathSource(path, isFontStyleEnable)
                return
            }
        } finally {
            fontSource.preLoad()
        }
    }

    override fun load(pagFile: PAGFile, callback: IConfigLoadCallback) {
        val textData = try {
            pagFile.getTextData(index)
        } catch (throwable: Throwable) {
            null
        }

        if (textData == null) {
            callback.onError(NullPointerException("No editable text was found. index=$index"))
            return
        }

        try {
            fontSource.loadFont(textData, object : IFontLoadCallback {
                override fun onResult(pagText: PAGText) {
                    loadFontSuccess(pagFile, textData, callback)
                }

                override fun onError(result: Throwable?) {
                    loadFontError(null, pagFile, textData, callback)
                }
            })
        } catch (throwable: Throwable) {
            loadFontError(throwable, pagFile, textData, callback)
        }
    }

    private fun replaceTextConfig(pagFile: PAGFile, textData: PAGText) {
        textData.text = data.getText()
        data.getPagTextSize()?.let {
            textData.fontSize = it
        }
        pagFile.replaceText(index, textData)
    }

    private fun loadFontSuccess(
        pagFile: PAGFile,
        textData: PAGText,
        callback: IConfigLoadCallback
    ) {
        try {
            replaceTextConfig(pagFile, textData)
            callback.onResult(pagFile)
        } catch (throwable: Throwable) {
            callback.onError(throwable)
        }
    }

    private fun loadFontError(
        throwable: Throwable?,
        pagFile: PAGFile,
        textData: PAGText,
        callback: IConfigLoadCallback
    ) {
        if (data.getPagTextFont()?.allowAnimationOnFontFailure() == true) {
            val errorMsg =
                "The font loading failed. Continue playing the animation. source=$fontSource"
            if (throwable == null) {
                CsLogger.tag(ImageLoaderConstants.TAG).w(errorMsg)
            } else {
                CsLogger.tag(ImageLoaderConstants.TAG).w(throwable, errorMsg)
            }
            loadFontSuccess(pagFile, textData, callback)
        } else {
            callback.onError(throwable)
        }
    }

    private class FontEmptySource : BaseFontSource(false) {
        override fun loadFont(pagText: PAGText, callback: IFontLoadCallback) {
            callback.onResult(pagText)
        }
    }
}
package com.proxy.service.imageloader.base.option.pag.txt

import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2025/10/13 20:32
 * @desc:
 */
class PagTextData private constructor(
    private val builder: IPagTextDataBuilderGet
) : IPagTextDataBuilderGet {

    companion object {
        fun builder(txt: String): IPagTextDataBuilder {
            return Builder(txt)
        }
    }

    override fun getText(): String {
        return builder.getText()
    }

    override fun getPagTextFont(): PagTextFont? {
        return builder.getPagTextFont()
    }

    override fun getPagTextSize(): Float? {
        return builder.getPagTextSize()
    }

    private class Builder(
        private val txt: String
    ) : IPagTextDataBuilder, IPagTextDataBuilderGet {

        private var font: PagTextFont? = null
        private var textSize: Float? = null

        override fun setPagTextFont(font: PagTextFont): IPagTextDataBuilder {
            this.font = font
            return this
        }

        override fun setPagTextSizePx(size: Float): IPagTextDataBuilder {
            if (size >= 0) {
                textSize = size
            }
            return this
        }

        override fun setPagTextSizeSp(size: Float): IPagTextDataBuilder {
            if (size >= 0) {
                textSize = CsDpUtils.sp2pxf(size)
            }
            return this
        }

        override fun setPagTextSizeDp(size: Float): IPagTextDataBuilder {
            if (size >= 0) {
                textSize = CsDpUtils.dp2pxf(size)
            }
            return this
        }

        override fun build(): PagTextData {
            return PagTextData(this)
        }

        override fun getText(): String {
            return txt
        }

        override fun getPagTextFont(): PagTextFont? {
            return font
        }

        override fun getPagTextSize(): Float? {
            return textSize
        }
    }
}




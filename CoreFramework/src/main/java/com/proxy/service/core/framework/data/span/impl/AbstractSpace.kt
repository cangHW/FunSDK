package com.proxy.service.core.framework.data.span.impl

import android.graphics.Color
import com.proxy.service.core.framework.data.span.builder.ISpaceBuilder
import com.proxy.service.core.framework.data.span.custom.SpaceSpan

/**
 * @author: cangHX
 * @data: 2025/7/14 10:10
 * @desc:
 */
abstract class AbstractSpace : AbstractBase(), ISpaceBuilder {

    companion object {
        private const val DEFAULT_SPACE_COLOR = Color.TRANSPARENT
    }

    protected var spaceSize: Int = 0
    private var spaceColor: Int = DEFAULT_SPACE_COLOR

    override fun setSpaceColor(color: Int): ISpaceBuilder {
        this.spaceColor = color
        return this
    }

    override fun applyLast() {
        super.applyLast()
        if (mType == TYPE_SPACE) {
            val start = mBuilder.length
            mBuilder.append("< >")
            val end = mBuilder.length
            mBuilder.setSpan(SpaceSpan(spaceSize, spaceColor), start, end, flag)
        }

        spaceSize = 0
        spaceColor = DEFAULT_SPACE_COLOR
    }

}
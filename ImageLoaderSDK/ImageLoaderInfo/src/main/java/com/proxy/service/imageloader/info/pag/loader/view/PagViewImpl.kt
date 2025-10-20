package com.proxy.service.imageloader.info.pag.loader.view

import android.content.Context
import android.util.AttributeSet
import org.libpag.PAGComposition
import org.libpag.PAGView

/**
 * @author: cangHX
 * @data: 2025/10/10 18:28
 * @desc:
 */
class PagViewImpl : PAGView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setComposition(p0: PAGComposition?) {
        super.setComposition(p0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        freeCache()
    }

}
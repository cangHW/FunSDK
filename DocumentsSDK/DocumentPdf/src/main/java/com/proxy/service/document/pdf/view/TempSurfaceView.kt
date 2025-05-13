package com.proxy.service.document.pdf.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import com.proxy.service.document.base.pdf.IPdfLoader
import com.proxy.service.document.pdf.R

/**
 * @author: cangHX
 * @data: 2025/5/12 09:44
 * @desc:
 */
class TempSurfaceView : FrameLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var surface: Surface? = null

    private fun init(context: Context) {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.cs_document_pdf_temp_surface_layout, null, false)

        val sfv = rootView.findViewById<SurfaceView>(R.id.sfv)
        sfv.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                surface = holder.surface
                showContent(0)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                surface = null
            }
        })


        addView(rootView)
    }

    private var loader: IPdfLoader? = null

    fun setLoader(loader: IPdfLoader?) {
        this.loader = loader
        showContent(0)
    }

    private var showIndex = 0

    fun showContent(index: Int) {
        val sf = surface ?: return
        val ld = loader ?: return

        if (index < 0) {
            return
        }

        if (index >= ld.getPageCount()) {
            return
        }

        showIndex = index
        ld.renderPageToSurface(index, sf, 100, 100, 800, 500, true, true)
    }

    fun getShowIndex():Int {
        return showIndex
    }

}
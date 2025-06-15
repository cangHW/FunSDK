package com.proxy.service.document.pdf.info.view.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.info.R

/**
 * @author: cangHX
 * @data: 2025/5/9 09:54
 * @desc:
 */
class TempView : FrameLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var adapter: TempAdapter? = null

    private fun init(context: Context) {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.cs_document_pdf_temp_layout, null, false)
        val vp = rootView.findViewById<ViewPager2>(R.id.vp2)
        adapter = TempAdapter()
        vp.adapter = adapter

        addView(rootView)
    }

    fun setLoader(loader: IPdfLoader?) {
        adapter?.setLoader(loader)
    }

    private class TempAdapter : RecyclerView.Adapter<TempViewHolder>() {

        private var loader: IPdfLoader? = null

        fun setLoader(loader: IPdfLoader?) {
            this.loader = loader
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempViewHolder {
            val rootView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cs_document_pdf_temp_item, parent, false)
            return TempViewHolder(rootView)
        }

        override fun getItemCount(): Int {
            return loader?.getPageCount() ?: 0
        }

        override fun onBindViewHolder(holder: TempViewHolder, position: Int) {
            holder.render(loader, position)
        }
    }

    private class TempViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            private val map = HashMap<Int, Bitmap>()
        }

        private var iv: AppCompatImageView? = null

        init {
            iv = itemView.findViewById(R.id.iv)
        }

        fun render(loader: IPdfLoader?, position: Int) {
            iv?.post {
                var bitmap = map.get(position)
                if (bitmap == null) {
                    val width = iv?.width ?: 100
                    val height = iv?.height ?: 100
                    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    loader?.renderPageToBitmap(position, bitmap, 0, 0, width, height, false)
//                    map.put(position, bitmap)
                    bitmap.recycle()
                }

//                iv?.setImageBitmap(bitmap)
//                loader?.getPageSizePixel(position)?.let {
//                    CsLogger.tag("asd").i("position = $position, width = ${it.width}, height = ${it.height}")
//                }
            }
        }

    }
}
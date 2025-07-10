package com.proxy.service.document.pdf.info.view.view

import android.content.Context
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
import com.proxy.service.document.pdf.info.view.cache.PartCache

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
            LayoutInflater.from(context).inflate(R.layout.cs_document_pdf_info_temp_layout, this, false)
        val vp = rootView.findViewById<ViewPager2>(R.id.vp2)
        adapter = TempAdapter()
        vp.adapter = adapter

        addView(rootView)
    }

    fun setLoader(loader: IPdfLoader?, cache: PartCache) {
        adapter?.setLoader(loader, cache)
    }

    private class TempAdapter : RecyclerView.Adapter<TempViewHolder>() {

        private var loader: IPdfLoader? = null
        private var cache: PartCache? = null

        fun setLoader(loader: IPdfLoader?, cache: PartCache) {
            this.loader = loader
            this.cache = cache
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempViewHolder {
            val rootView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cs_document_pdf_info_temp_item, parent, false)
            return TempViewHolder(rootView)
        }

        override fun getItemCount(): Int {
            return loader?.getPageCount() ?: 0
        }

        override fun onBindViewHolder(holder: TempViewHolder, position: Int) {
            holder.render(cache, position)
        }
    }

    private class TempViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var iv: AppCompatImageView = itemView.findViewById(R.id.iv)

        fun render(cache: PartCache?, position: Int) {
            iv.post {
                cache?.getPart(position, iv.width, iv.height)?.bitmap?.let {
                    iv.setImageBitmap(it)
                }
            }
        }

    }
}
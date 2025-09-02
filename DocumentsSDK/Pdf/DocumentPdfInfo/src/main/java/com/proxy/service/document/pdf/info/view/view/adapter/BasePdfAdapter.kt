package com.proxy.service.document.pdf.info.view.view.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.callback.OnShouldCreateCoverViewCallback
import com.proxy.service.document.pdf.info.view.cache.PartCache

/**
 * @author: cangHX
 * @data: 2025/7/22 20:55
 * @desc:
 */
abstract class BasePdfAdapter : RecyclerView.Adapter<BasePdfAdapter.BasePdfViewHolder>() {

    protected var loader: IPdfLoader? = null
    protected var cache: PartCache? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setLoader(loader: IPdfLoader?, cache: PartCache) {
        this.loader = loader
        this.cache = cache
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BasePdfViewHolder, position: Int) {
        holder.render(cache, position)
    }

    abstract class BasePdfViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), PartCache.OnRefreshCallback {

        abstract fun render(cache: PartCache?, position: Int)

    }

}
package com.proxy.service.document.pdf.info.view.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.proxy.service.document.pdf.base.view.callback.OnShouldCreateCoverViewCallback
import com.proxy.service.document.pdf.info.R
import com.proxy.service.document.pdf.info.view.cache.PartCache

/**
 * @author: cangHX
 * @data: 2025/7/22 20:54
 * @desc:
 */
class SingePageAdapter(
    private val callback: OnShouldCreateCoverViewCallback?,
    private val maxShowCount: Int
) : BasePdfAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingePageViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cs_document_pdf_info_item_single_page, parent, false)
        cache?.getRenderConfig()?.viewBackgroundColor?.let {
            rootView.setBackgroundColor(it.toInt())
        }
        return SingePageViewHolder(rootView, callback)
    }

    override fun getItemCount(): Int {
        val pageTotalCount = loader?.getPageCount() ?: 0
        return Math.min(maxShowCount, pageTotalCount)
    }

    class SingePageViewHolder(
        itemView: View,
        private val callback: OnShouldCreateCoverViewCallback?
    ) : BasePdfViewHolder(itemView) {

        private var iv: AppCompatImageView = itemView.findViewById(R.id.iv)
        private var coverLayout: FrameLayout = itemView.findViewById(R.id.cover_layout)

        private var width = -1
        private var height = -1

        override fun render(cache: PartCache?, position: Int) {
            coverLayout.removeAllViews()
            callback?.onShouldShowCoverView(position, coverLayout)

            if (width == -1 || height == -1) {
                iv.post {
                    width = iv.width
                    height = iv.height
                    showPage(cache, position, width, height)
                }
            } else {
                showPage(cache, position, width, height)
            }
        }

        private fun showPage(cache: PartCache?, position: Int, width: Int, height: Int) {
            if (cache == null) {
                return
            }
            val part = cache.getPart(position, width, height, this)
            part?.bitmap?.let {
                iv.setImageBitmap(it)
            }
        }

        override fun onRefresh(position: Int) {
            iv.postInvalidate()
        }
    }

}
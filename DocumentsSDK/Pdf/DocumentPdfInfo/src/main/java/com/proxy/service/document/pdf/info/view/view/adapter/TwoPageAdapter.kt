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
class TwoPageAdapter(
    private val callback: OnShouldCreateCoverViewCallback?,
    private val maxShowCount: Int
) : BasePdfAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoPageViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cs_document_pdf_info_item_two_page, parent, false)
        cache?.getRenderConfig()?.viewBackgroundColor?.let {
            rootView.setBackgroundColor(it.toInt())
        }
        return TwoPageViewHolder(rootView, callback)
    }

    override fun getItemCount(): Int {
        val pageTotalCount = ((loader?.getPageCount() ?: 0) + 1) / 2
        if (maxShowCount == Int.MAX_VALUE) {
            return pageTotalCount
        }
        val limitCount = (maxShowCount + 1) / 2
        return Math.min(limitCount, pageTotalCount)
    }

    class TwoPageViewHolder(
        itemView: View,
        private val callback: OnShouldCreateCoverViewCallback?
    ) : BasePdfViewHolder(itemView) {

        private var left: AppCompatImageView = itemView.findViewById(R.id.left)
        private var right: AppCompatImageView = itemView.findViewById(R.id.right)

        private var coverLayout: FrameLayout = itemView.findViewById(R.id.cover_layout)

        private var leftWidth = -1
        private var leftHeight = -1

        private var rightWidth = -1
        private var rightHeight = -1

        override fun render(cache: PartCache?, position: Int) {
            coverLayout.removeAllViews()
            callback?.onShouldShowCoverView(position, coverLayout)

            val leftPosition = position * 2
            if (leftWidth == -1 || leftHeight == -1) {
                left.post {
                    leftWidth = left.width
                    leftHeight = left.height
                    showPage(left, cache, leftPosition, leftWidth, leftHeight)
                }
            } else {
                showPage(left, cache, leftPosition, leftWidth, leftHeight)
            }

            val rightPosition = leftPosition + 1
            if (rightWidth == -1 || rightHeight == -1) {
                right.post {
                    rightWidth = right.width
                    rightHeight = right.height
                    showPage(right, cache, rightPosition, rightWidth, rightHeight)
                }
            } else {
                showPage(right, cache, rightPosition, rightWidth, rightHeight)
            }
        }

        private fun showPage(
            view: AppCompatImageView,
            cache: PartCache?,
            position: Int,
            width: Int,
            height: Int
        ) {
            if (cache == null) {
                return
            }
            val part = cache.getPart(position, width, height, this)
            part?.bitmap?.let {
                view.setImageBitmap(it)
            }
        }

        override fun onRefresh(position: Int) {
            if (position % 2 == 0) {
                left.postInvalidate()
            } else {
                right.postInvalidate()
            }
        }
    }

}
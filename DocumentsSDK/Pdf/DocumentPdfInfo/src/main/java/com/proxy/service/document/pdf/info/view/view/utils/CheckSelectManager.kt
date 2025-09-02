package com.proxy.service.document.pdf.info.view.view.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * @author: cangHX
 * @data: 2025/7/24 17:02
 * @desc:
 */
class CheckSelectManager(private val callback: OnSelectChangedCallback) {

    interface OnSelectChangedCallback {
        fun onItemSelect(position: Int)
    }

    private var currentVisibleItem = 0

    fun setView(view: RecyclerView?) {
        val lm = (view?.layoutManager as? LinearLayoutManager?) ?: return

        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisiblePosition: Int = lm.findFirstVisibleItemPosition()
                val lastVisiblePosition: Int = lm.findLastVisibleItemPosition()

                val visibleItem = (firstVisiblePosition + lastVisiblePosition) / 2

                if (currentVisibleItem == visibleItem) {
                    return
                }
                currentVisibleItem = visibleItem
                callback.onItemSelect(visibleItem)
            }
        })
    }


    fun setView(view: ViewPager2?) {
        view?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (currentVisibleItem == position) {
                    return
                }
                currentVisibleItem = position
                callback.onItemSelect(position)
            }
        })
    }

}
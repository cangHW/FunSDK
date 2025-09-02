package com.proxy.service.document.pdf.info.view.view.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.info.R
import com.proxy.service.document.pdf.info.view.cache.PartCache
import com.proxy.service.document.pdf.info.view.view.adapter.TwoPageAdapter
import com.proxy.service.document.pdf.info.view.view.utils.CheckSelectManager

/**
 * @author: cangHX
 * @data: 2025/7/24 15:08
 * @desc:
 */
class TwoPageLandscape : BaseShow(), CheckSelectManager.OnSelectChangedCallback {

    private var viewPager: ViewPager2? = null
    private var adapter: TwoPageAdapter? = null

    private val manager = CheckSelectManager(this)

    override fun setViewGroup(viewGroup: ViewGroup) {
        viewGroup.removeAllViews()

        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(
                R.layout.cs_document_pdf_info_layout_viewpager,
                viewGroup,
                false
            )
        viewPager = rootView.findViewById(R.id.view_pager)
        adapter = TwoPageAdapter(createCoverViewCallback, maxShowCount)
        viewPager?.adapter = adapter

        manager.setView(viewPager)

        viewGroup.addView(rootView)
    }

    override fun show(loader: IPdfLoader, cache: PartCache, showIndex: Int) {
        adapter?.setLoader(loader, cache)
        val realShowIndex = showIndex / 2
        viewPager?.setCurrentItem(realShowIndex, false)
        if (realShowIndex == 0) {
            onItemSelect(0)
        }
    }

    override fun onItemSelect(position: Int) {
        pageShowCallback?.onPageShow(position * 2)
    }
}
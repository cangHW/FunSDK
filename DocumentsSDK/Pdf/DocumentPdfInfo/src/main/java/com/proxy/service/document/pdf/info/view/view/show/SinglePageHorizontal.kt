package com.proxy.service.document.pdf.info.view.view.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.info.R
import com.proxy.service.document.pdf.info.view.cache.PartCache
import com.proxy.service.document.pdf.info.view.view.adapter.SingePageAdapter
import com.proxy.service.document.pdf.info.view.view.utils.CheckSelectManager

/**
 * @author: cangHX
 * @data: 2025/7/21 18:46
 * @desc:
 */
class SinglePageHorizontal : BaseShow(), CheckSelectManager.OnSelectChangedCallback {

    private var viewPager: ViewPager2? = null
    private var adapter: SingePageAdapter? = null

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
        adapter = SingePageAdapter(createCoverViewCallback, maxShowCount)
        viewPager?.adapter = adapter

        manager.setView(viewPager)

        viewGroup.addView(rootView)
    }

    override fun show(loader: IPdfLoader, cache: PartCache, showIndex: Int) {
        adapter?.setLoader(loader, cache)
        viewPager?.setCurrentItem(showIndex, false)
        if (showIndex == 0) {
            onItemSelect(0)
        }
    }

    override fun onItemSelect(position: Int) {
        pageShowCallback?.onPageShow(position)
    }
}
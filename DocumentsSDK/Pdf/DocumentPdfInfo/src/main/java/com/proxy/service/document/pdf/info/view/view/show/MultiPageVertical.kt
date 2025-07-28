package com.proxy.service.document.pdf.info.view.view.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class MultiPageVertical : BaseShow(), CheckSelectManager.OnSelectChangedCallback {

    private var recyclerView: RecyclerView? = null
    private var adapter: SingePageAdapter? = null

    private val manager = CheckSelectManager(this)

    override fun setViewGroup(viewGroup: ViewGroup) {
        viewGroup.removeAllViews()

        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(
                R.layout.cs_document_pdf_info_layout_recyclerview,
                viewGroup,
                false
            )
        recyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(
            viewGroup.context,
            RecyclerView.VERTICAL,
            false
        )
        adapter = SingePageAdapter(createCoverViewCallback, maxShowCount)
        recyclerView?.adapter = adapter

        manager.setView(recyclerView)

        viewGroup.addView(rootView)
    }

    override fun show(loader: IPdfLoader, cache: PartCache, showIndex: Int) {
        adapter?.setLoader(loader, cache)
        recyclerView?.scrollToPosition(showIndex)
    }

    override fun onItemSelect(position: Int) {
        pageShowCallback?.onPageShow(position)
    }
}
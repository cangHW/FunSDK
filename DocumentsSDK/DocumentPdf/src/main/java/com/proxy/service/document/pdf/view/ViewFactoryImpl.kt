package com.proxy.service.document.pdf.view

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.document.base.pdf.config.PdfConfig
import com.proxy.service.document.base.pdf.view.IPdfView
import com.proxy.service.document.base.pdf.view.IViewFactory
import com.proxy.service.document.pdf.PdfServiceImpl
import com.proxy.service.document.pdf.lifecycle.LifecycleManager
import com.proxy.service.document.pdf.view.config.RenderConfig
import com.proxy.service.document.pdf.view.group.IFactory
import com.proxy.service.document.pdf.view.view.FinalView

/**
 * @author: cangHX
 * @data: 2025/5/15 09:47
 * @desc:
 */
class ViewFactoryImpl(private val config: PdfConfig) : IViewFactory {

    private var owner: LifecycleOwner? = null
    private val renderConfig = RenderConfig()

    override fun setViewBackgroundColor(color: Long): IViewFactory {
        renderConfig.viewBackgroundColor = color
        return this
    }

    override fun setPageBackgroundColor(color: Long): IViewFactory {
        renderConfig.viewBackgroundColor = color
        return this
    }

    override fun setLifecycleOwner(owner: LifecycleOwner): IViewFactory {
        this.owner = owner
        return this
    }

    override fun into(viewGroup: ViewGroup): IPdfView {
        return createPdfView(viewGroup)
    }

    private fun createPdfView(viewGroup: ViewGroup?): IPdfView {
        val service = PdfServiceImpl()
        val loader = service.createLoader(config)
        LifecycleManager.instance.bindLifecycle(owner, loader)

        val context = viewGroup?.context ?: CsContextManager.getApplication()
        val pdfView = FinalView(context)
        addViewToGroup(pdfView, viewGroup)
        pdfView.setLoader(renderConfig, loader)
        return PdfViewImpl(pdfView)
    }

    private fun addViewToGroup(view: View, group: ViewGroup?) {
        group?.let {
            IFactory.of(it, view)
        }
    }
}
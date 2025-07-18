package com.proxy.service.document.pdf.info.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.config.info.FailedResult
import com.proxy.service.document.pdf.base.config.source.BaseSource
import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.base.enums.PagePixelFormat
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IPdfView
import com.proxy.service.document.pdf.base.view.IViewLoader
import com.proxy.service.document.pdf.info.PdfServiceImpl
import com.proxy.service.document.pdf.info.loader.impl.PdfLoader
import com.proxy.service.document.pdf.info.view.cache.PartCache
import com.proxy.service.document.pdf.info.view.cache.CacheType
import com.proxy.service.document.pdf.info.view.lifecycle.LifecycleManager
import com.proxy.service.document.pdf.info.view.config.RenderConfig

/**
 * @author: cangHX
 * @data: 2025/5/15 09:47
 * @desc:
 */
class ViewLoaderImpl(private val config: PdfConfig) : IViewLoader {

    private val renderConfig = RenderConfig()

    private var owner: LifecycleOwner? = null
    private var loadStateCallback: LoadStateCallback? = null

    private var cacheType: CacheType = CacheType.COUNT
    private var maxCacheCount: Int = PdfConstants.DEFAULT_PAGE_CACHE_MAX_COUNT
    private var maxCacheSize: Int = 0

    override fun setViewBackgroundColor(color: Long): IViewLoader {
        renderConfig.viewBackgroundColor = color
        return this
    }

    override fun setPageBackgroundColor(color: Long): IViewLoader {
        renderConfig.viewBackgroundColor = color
        return this
    }

    override fun setPageCacheMaxCount(maxCount: Int): IViewLoader {
        this.maxCacheCount = Math.max(maxCount, PdfConstants.PAGE_CACHE_MIN_COUNT)
        this.cacheType = CacheType.COUNT
        return this
    }

    override fun setPageCacheMaxSize(maxSize: Int): IViewLoader {
        this.maxCacheSize = Math.max(maxSize, PdfConstants.PAGE_CACHE_MIN_SIZE)
        this.cacheType = CacheType.SIZE
        return this
    }

    override fun setPagePixelFormat(format: PagePixelFormat): IViewLoader {
        renderConfig.format = format
        return this
    }

    override fun setLifecycleOwner(owner: LifecycleOwner): IViewLoader {
        this.owner = owner
        return this
    }

    override fun setLoadStateCallback(callback: LoadStateCallback): IViewLoader {
        this.loadStateCallback = callback
        return this
    }

    override fun into(viewGroup: ViewGroup): IPdfView {
        return createPdfView(viewGroup)
    }

    private fun createPdfView(viewGroup: ViewGroup): IPdfView {
        val pdf = PdfViewImpl(viewGroup)
        val loader = PdfLoader()
        val cache = if (cacheType == CacheType.SIZE) {
            PartCache.create(maxCacheSize, CacheType.SIZE, renderConfig, loader)
        } else {
            PartCache.create(maxCacheCount, CacheType.COUNT, renderConfig, loader)
        }
        LifecycleManager.instance.bindLifecycle(owner, loader, cache)

        loader.setSourceList(config.getSources(), object : LoadStateCallback {
            override fun onLoadStart(sources: List<BaseSource>) {
                loadStateCallback?.onLoadStart(sources)
            }

            override fun onLoadComplete(success: List<BaseSource>, failed: List<FailedResult>) {
                if (success.isNotEmpty()) {
                    pdf.setLoader(loader, cache)
                }
                loadStateCallback?.onLoadComplete(success, failed)
            }
        })
        return pdf
    }
}
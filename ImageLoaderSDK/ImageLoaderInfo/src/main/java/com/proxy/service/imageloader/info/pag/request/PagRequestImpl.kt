package com.proxy.service.imageloader.info.pag.request

import com.proxy.service.imageloader.base.option.pag.IPageOption
import com.proxy.service.imageloader.base.request.pag.IPagRequest
import com.proxy.service.imageloader.info.pag.info.PagInfo
import com.proxy.service.imageloader.info.pag.option.PagOptionImpl
import com.proxy.service.imageloader.info.pag.request.source.AssetPagSource
import com.proxy.service.imageloader.info.pag.request.source.PathPagSource
import com.proxy.service.imageloader.info.pag.request.source.ResPagSource
import com.proxy.service.imageloader.info.pag.request.source.UrlPagSource

/**
 * @author: cangHX
 * @data: 2025/10/10 15:10
 * @desc:
 */
class PagRequestImpl(
    private val info: PagInfo
) : IPagRequest {

    override fun loadUrl(url: String): IPageOption {
        info.sourceData = UrlPagSource(url, null)
        return PagOptionImpl(info)
    }

    override fun loadUrl(url: String, cacheKey: String): IPageOption {
        info.sourceData = UrlPagSource(url, cacheKey)
        return PagOptionImpl(info)
    }

    override fun loadPath(path: String): IPageOption {
        info.sourceData = PathPagSource(path)
        return PagOptionImpl(info)
    }

    override fun loadAsset(fileName: String): IPageOption {
        info.sourceData = AssetPagSource(fileName)
        return PagOptionImpl(info)
    }

    override fun loadRes(resourceId: Int): IPageOption {
        info.sourceData = ResPagSource(resourceId)
        return PagOptionImpl(info)
    }
}
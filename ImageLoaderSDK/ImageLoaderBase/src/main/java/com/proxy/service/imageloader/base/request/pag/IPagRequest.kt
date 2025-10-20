package com.proxy.service.imageloader.base.request.pag

import androidx.annotation.RawRes
import com.proxy.service.imageloader.base.option.pag.IPageOption
import com.proxy.service.imageloader.base.request.base.IRequest

/**
 * @author: cangHX
 * @data: 2025/10/10 14:58
 * @desc:
 */
interface IPagRequest : IRequest<IPageOption> {

    /**
     * 加载网络文件, 支持同一链接指向不同内容
     *
     * @param cacheKey   自定义资源唯一标识
     * */
    fun loadUrl(url: String, cacheKey: String): IPageOption

    /**
     * 加载资源文件
     * */
    fun loadRes(@RawRes resourceId: Int): IPageOption

}
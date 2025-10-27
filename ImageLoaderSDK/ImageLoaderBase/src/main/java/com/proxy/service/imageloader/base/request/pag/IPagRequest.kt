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
     * 加载资源文件
     * */
    fun loadRes(@RawRes resourceId: Int): IPageOption

}
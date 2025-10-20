package com.proxy.service.imageloader.info.pag.request.source

import android.content.Context
import org.libpag.PAGFile

/**
 * @author: cangHX
 * @data: 2025/10/10 16:56
 * @desc:
 */
class AssetPagSource(
    private val fileName: String
) : BasePagSourceData() {

    override fun load(context: Context?, listener: IPagLoadCallback?) {
        try {
            val file = PAGFile.Load(context?.assets, fileName)
            if (file == null) {
                listener?.onError(null)
                return
            }
            listener?.onResult(file)
        } catch (throwable: Throwable) {
            listener?.onError(throwable)
        }
    }
}
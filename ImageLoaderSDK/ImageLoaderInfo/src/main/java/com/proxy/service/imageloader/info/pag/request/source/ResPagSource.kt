package com.proxy.service.imageloader.info.pag.request.source

import android.content.Context
import androidx.annotation.RawRes
import org.libpag.PAGFile

/**
 * @author: cangHX
 * @data: 2025/10/10 16:57
 * @desc:
 */
class ResPagSource(
    @RawRes private val resourceId: Int
) : BasePagSourceData() {

    override fun load(context: Context?, listener: IPagLoadCallback?) {
        try {
            val inputStream = context?.resources?.openRawResource(resourceId)
            val byteArray = inputStream.use {
                it?.readBytes()
            }
            if (byteArray == null) {
                listener?.onError(null)
                return
            }
            val file = PAGFile.Load(byteArray)
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
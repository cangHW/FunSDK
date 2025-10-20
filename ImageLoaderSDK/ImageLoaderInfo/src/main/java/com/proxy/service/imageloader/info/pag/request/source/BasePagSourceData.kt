package com.proxy.service.imageloader.info.pag.request.source

import android.content.Context
import org.libpag.PAGFile

/**
 * @author: cangHX
 * @data: 2025/10/10 15:14
 * @desc:
 */
abstract class BasePagSourceData {

    interface IPagLoadCallback {

        fun onResult(result: PAGFile)

        fun onError(result: Throwable?)

    }

    abstract fun load(context: Context?, listener: IPagLoadCallback?)

}
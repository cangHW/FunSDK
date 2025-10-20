package com.proxy.service.imageloader.info.pag.option.config

import org.libpag.PAGFile

/**
 * @author: cangHX
 * @data: 2025/10/13 20:41
 * @desc:
 */
abstract class BaseConfig {

    interface IConfigLoadCallback {

        fun onResult(result: PAGFile)

        fun onError(result: Throwable?)

    }

    /**
     * 预加载
     * */
    open fun tryPreLoad() {}

    /**
     * 加载配置
     * */
    abstract fun load(pagFile: PAGFile, callback: IConfigLoadCallback)

}
package com.proxy.service.core.framework.io.zip

/**
 * @author: cangHX
 * @data: 2024/4/30 20:50
 * @desc:
 */
interface ZipProgressCallback {

    /**
     * @param progress [0, 100]
     * */
    fun onProgress(progress: Int)

}
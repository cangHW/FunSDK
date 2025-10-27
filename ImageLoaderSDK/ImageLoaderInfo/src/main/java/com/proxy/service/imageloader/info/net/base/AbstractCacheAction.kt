package com.proxy.service.imageloader.info.net.base

import java.io.File

/**
 * @author: cangHX
 * @data: 2025/10/27 10:18
 * @desc:
 */
abstract class AbstractCacheAction {

    /**
     * 从缓存中获取文件 file
     * */
     abstract fun getFileFromCache(key: String): File

    /**
     * 更新缓存中的文件信息
     * */
     abstract fun updateInfoForCache(key: String)

}
package com.proxy.service.apm.info.cache

import com.proxy.service.core.framework.io.monitor.info.FileInfo

/**
 * @author: cangHX
 * @date: 2026/6/1 18:27
 * @desc:
 */
interface ExceptionHandler {

    /**
     * @param filePaths 异常信息记录文件
     * */
    fun onException(filePaths: List<FileInfo>)

}
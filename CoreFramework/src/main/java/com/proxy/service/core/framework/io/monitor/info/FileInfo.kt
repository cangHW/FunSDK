package com.proxy.service.core.framework.io.monitor.info

/**
 * @author: cangHX
 * @date: 2025/4/23 11:05
 * @desc:
 */
class FileInfo {

    /**
     * 文件名称
     * */
    var fileName = ""

    /**
     * 文件路径
     * */
    var filePath = ""

    /**
     * 文件长度
     * */
    var fileLength = 0L

    /**
     * 文件最后修改时间
     * */
    var lastModified = 0L

    override fun toString(): String {
        return "FileInfo(fileName='$fileName', filePath='$filePath', fileLength=$fileLength, lastModified=$lastModified)"
    }

}
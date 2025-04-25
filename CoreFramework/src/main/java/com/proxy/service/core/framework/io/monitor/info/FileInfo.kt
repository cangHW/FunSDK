package com.proxy.service.core.framework.io.monitor.info

/**
 * @author: cangHX
 * @data: 2025/4/23 11:05
 * @desc:
 */
class FileInfo {

    var fileName = ""

    var filePath = ""

    var fileLength = 0L

    var lastModified = 0L

    override fun toString(): String {
        return "FileInfo(fileName='$fileName', filePath='$filePath', fileLength=$fileLength, lastModified=$lastModified)"
    }

}
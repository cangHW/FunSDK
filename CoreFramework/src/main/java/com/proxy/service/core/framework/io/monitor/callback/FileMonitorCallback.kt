package com.proxy.service.core.framework.io.monitor.callback

import com.proxy.service.core.framework.io.monitor.info.FileInfo

/**
 * @author: cangHX
 * @data: 2025/4/23 11:15
 * @desc:
 */
interface FileMonitorCallback {

    /**
     * 开始监听文件变更
     * */
    fun onStart(totalFileInfos: ArrayList<FileInfo>) {}

    /**
     * 新增文件
     * */
    fun onFileAdded(totalFileInfos: ArrayList<FileInfo>, filePaths: List<String>) {}

    /**
     * 文件发生变化
     * */
    fun onFileChanged(totalFileInfos: ArrayList<FileInfo>) {}

    /**
     * 移除文件
     * */
    fun onFileRemoved(totalFileInfos: ArrayList<FileInfo>, filePaths: List<String>) {}

    /**
     * 结束监听文件变更
     * */
    fun onClose(totalFileInfos: ArrayList<FileInfo>) {}
}
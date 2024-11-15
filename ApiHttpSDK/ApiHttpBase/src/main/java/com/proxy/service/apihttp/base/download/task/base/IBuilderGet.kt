package com.proxy.service.apihttp.base.download.task.base

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IBuilderGet {

    /**
     * 获取组名称
     * */
    fun getGroupName(): String

    /**
     * 获取任务唯一标签
     * */
    fun getTaskTag(): String

    /**
     * 获取任务优先级
     * */
    fun getPriority(): Int

    /**
     * 获取下载文件 URL
     * */
    fun getDownloadUrl(): String

    /**
     * 获取下载文件全路径
     * */
    fun getFilePath(): String

    /**
     * 获取下载文件文件夹路径
     * */
    fun getFileDir(): String

    /**
     * 获取下载文件名称
     * */
    fun getFileName(): String

    /**
     * 获取下载文件 md5 信息
     * */
    fun getFileMd5(): String

    /**
     * 获取下载文件总大小
     * */
    fun getFileSize(): Long

    /**
     * 获取是否开启多线程分片下载
     * */
    fun getMultiPartEnable(): Boolean

}
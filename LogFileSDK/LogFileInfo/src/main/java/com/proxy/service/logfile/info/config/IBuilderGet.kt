package com.proxy.service.logfile.info.config

/**
 * @author: cangHX
 * @data: 2025/1/16 19:48
 * @desc:
 */
interface IBuilderGet {

    /**
     * 获取是否同步
     * */
    fun getSyncMode(): Boolean

    /**
     * 获取文件路径
     * */
    fun getLogDir(): String

    /**
     * 获取文件前缀
     * */
    fun getFileNamePrefix(): String

    /**
     * 获取文件后缀
     * */
    fun getFileNamePostfix(): String

    /**
     * 获取文件缓存时长
     * */
    fun getCacheTime(): Long

    /**
     * 获取日志类型
     * */
    fun getLogType(): Int

    /**
     * 获取文件最大长度
     * */
    fun getMaxFileSize(): Long

    /**
     * 获取缓存文件最大数量
     * */
    fun getMaxFiles(): Int

    /**
     * 获取新文件创建时间节点
     * */
    fun getDailyHour(): Int

    /**
     * 获取新文件创建时间节点
     * */
    fun getDailyMinute(): Int
}
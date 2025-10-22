package com.proxy.service.logfile.info.config

import com.proxy.service.logfile.info.manager.CompressionMode
import com.proxy.service.logfile.info.manager.EncryptionMode

/**
 * @author: cangHX
 * @data: 2025/1/16 19:48
 * @desc:
 */
interface IBuilderGet {

    /**
     * 周期性自动刷磁盘的间隔
     * */
    fun getFlushEveryTime(): Long

    /**
     * 获取是否同步
     * */
    fun getSyncMode(): Boolean

    fun getCompressionMode(): CompressionMode

    fun getEncryptionMode(): EncryptionMode

    fun getEncryptionKey(): String

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
     * 获取清理任务执行间隔时长
     * */
    fun getCleanTaskIntervalTime(): Long

    /**
     * 获取日志类型
     * */
    fun getLogType(): Int

    /**
     * 获取文件最大长度
     * */
    fun getSingleFileMaxSize(): Long

    /**
     * 获取缓存文件最大数量
     * */
    fun getMaxFileCount(): Int

    /**
     * 获取新文件创建时间节点
     * */
    fun getDailyHour(): Int

    /**
     * 获取新文件创建时间节点
     * */
    fun getDailyMinute(): Int
}
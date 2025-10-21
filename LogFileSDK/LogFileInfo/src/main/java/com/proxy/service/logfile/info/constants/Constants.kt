package com.proxy.service.logfile.info.constants

/**
 * @author: cangHX
 * @data: 2025/1/16 20:19
 * @desc:
 */
object Constants {

    const val TAG = "LogFile"

    /**
     * 周期性自动刷磁盘的间隔时间
     * */
    const val FLUSH_EVERY_TIME: Long = 0

    /**
     * 默认配置
     * */
    const val TYPE_NORMAL = 0

    /**
     * 按大小分割文件以及限制文件最大缓存数量配置
     * */
    const val TYPE_ROTATING = 1

    /**
     * 按天分割文件配置
     * */
    const val TYPE_DAILY = 2

    /**
     * 日志写入模式
     * */
    const val IS_SYNC_MODE = false

    /**
     * 日志文件名称前后缀
     * */
    const val NAME_PREFIX = "log"
    const val NAME_POSTFIX = ".log"

    /**
     * 日志文件缓存时长与清理任务执行间隔时长
     * */
    const val CACHE_TIME = 7 * 24 * 60 * 60 * 1000L
    const val CLEAN_TASK_INTERVAL_TIME = 24 * 60 * 60 * 1000L

    /**
     * 按大小分割文件以及限制文件最大缓存数量
     * */
    const val SINGLE_FILE_MAX_SIZE = 5 * 1024 * 1024L
    const val MAX_FILE_COUNT = 3

    /**
     * 按天分割文件
     * */
    const val HOUR = 0
    const val MINUTE = 0
}
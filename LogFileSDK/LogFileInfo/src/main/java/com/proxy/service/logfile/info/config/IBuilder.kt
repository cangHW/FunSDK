package com.proxy.service.logfile.info.config

import java.util.concurrent.TimeUnit
import com.proxy.service.logfile.info.constants.Constants

/**
 * @author: cangHX
 * @data: 2025/1/16 19:48
 * @desc: 千条日志耗时: 7 毫秒左右
 */
interface IBuilder {

    /**
     * 设置日志模式, 是否同步模式, 默认: [[Constants.IS_SYNC_MODE]
     *
     * @desc 异步模式性能最高
     * */
    fun setLogMode(isSyncMode: Boolean): IBuilder

    /**
     * 设置日志文件路径, 默认: /storage/emulated/0/Android/<app package>/file/logfile/
     * */
    fun setLogDir(dir: String): IBuilder

    /**
     * 设置日志文件名称前缀, 默认: [Constants.NAME_PREFIX]
     * */
    fun setFileNamePrefix(namePrefix: String): IBuilder

    /**
     * 设置日志文件名称后缀, 默认: [Constants.NAME_POSTFIX]
     * */
    fun setFileNamePostfix(namePostfix: String): IBuilder

    /**
     * 设置日志文件缓存时长, 默认: [Constants.CACHE_TIME]
     * */
    fun setCacheTime(time: Long, unit: TimeUnit): IBuilder

    /**
     * 设置清理任务执行间隔时长, 默认: [Constants.CLEAN_TASK_INTERVAL_TIME]
     * */
    fun setCleanTaskIntervalTime(time: Long, unit: TimeUnit): IBuilder

    /**
     * 创建一个默认配置
     * */
    fun createNormalType(): LogConfig

    /**
     * 创建一个按大小分割文件以及限制文件最大缓存数量配置
     *
     * @param singleFileMaxSize 单个文件最大长度, 默认: [Constants.SINGLE_FILE_MAX_SIZE]
     * @param maxFileCount      最大文件数量, 默认: [Constants.MAX_FILE_COUNT]
     * */
    fun createRotatingType(singleFileMaxSize: Long, maxFileCount: Int): LogConfig

    /**
     * 创建一个按天分割文件配置
     *
     * @param hour  文件创建时间节点, 默认: [Constants.HOUR]
     * @param minute  文件创建时间节点, 默认: [Constants.MINUTE]
     *
     * @Desc hour 0, minute 0 代表每天 0点0分之后 如果触发日志写入会自动创建一个新文件
     * */
    fun createDailyType(hour: Int, minute: Int): LogConfig
}
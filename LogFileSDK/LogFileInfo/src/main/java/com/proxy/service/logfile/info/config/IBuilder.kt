package com.proxy.service.logfile.info.config

import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/1/16 19:48
 * @desc: 千条日志耗时: 7 毫秒左右
 */
interface IBuilder {

    /**
     * 设置日志模式, 是否同步模式, 默认: false
     *
     * @desc 异步模式性能最高
     * */
    fun setLogMode(isSyncMode: Boolean): IBuilder

    /**
     * 设置日志文件路径, 默认: /storage/emulated/0/Android/<app package>/file/logfile/
     * */
    fun setLogDir(dir: String): IBuilder

    /**
     * 设置日志文件名称前缀, 默认: log
     * */
    fun setFileNamePrefix(namePrefix: String): IBuilder

    /**
     * 设置日志文件名称后缀, 默认: .log
     * */
    fun setFileNamePostfix(namePostfix: String): IBuilder

    /**
     * 设置日志文件缓存时长, 默认: 7 天
     * */
    fun setCacheTime(time: Long, unit: TimeUnit): IBuilder

    /**
     * 创建一个默认配置
     * */
    fun createNormalType(): LogConfig

    /**
     * 创建一个按大小分割文件以及限制文件最大缓存数量配置, 默认: maxFileSize 5M, maxFiles 3
     * */
    fun createRotatingType(maxFileSize: Long, maxFiles: Int): LogConfig

    /**
     * 创建一个按天分割文件配置, 默认: hour 0, minute 0.
     *
     * @Desc hour 0, minute 0 代表每天 0点0分 自动创建一个新文件
     * */
    fun createDailyType(hour: Int, minute: Int): LogConfig
}
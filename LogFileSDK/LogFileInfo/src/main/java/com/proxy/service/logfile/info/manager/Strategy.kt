package com.proxy.service.logfile.info.manager

import com.proxy.service.logfile.info.constants.Constants

/**
 * @author: cangHX
 * @data: 2025/1/17 14:40
 * @desc:
 */
class Strategy {
    var _pkg: String = ""

    /**
     * 周期性自动刷磁盘的间隔时间
     * */
    var _flushEveryTime: Long = 0

    /**
     * 同步模式
     * */
    var _isSyncMode: Boolean = false

    /**
     * 保存路径
     * */
    var _dir: String = ""

    /**
     * 日志文件前后缀
     * */
    var _namePrefix: String = ""
    var _namePostfix: String = ""

    /**
     * 缓存时长与清理任务执行间隔时长
     * */
    var _cacheTime: Long = 0
    var _cleanTaskIntervalTime: Long = 0

    /**
     * 日志记录类型
     * */
    var _type: Int = Constants.TYPE_NORMAL

    /**
     * 单个文件最大长度与最大文件数量 [Constants.TYPE_ROTATING]
     * */
    var _singleFileMaxSize: Long = 0
    var _maxFileCount: Int = 0

    /**
     * 文件创建时间节点 [Constants.TYPE_DAILY]
     * */
    var _hour: Int = 0
    var _minute: Int = 0

    fun getPackageName(): String {
        return _pkg
    }

    fun getFlushEveryTime(): Long {
        return _flushEveryTime
    }

    fun isSyncMode(): Boolean {
        return _isSyncMode
    }

    fun getDir(): String {
        return _dir
    }

    fun getNamePrefix(): String {
        return _namePrefix
    }

    fun getNamePostfix(): String {
        return _namePostfix
    }

    fun getCacheTime(): Long {
        return _cacheTime
    }

    fun getCleanTaskIntervalTime(): Long {
        return _cleanTaskIntervalTime
    }

    fun getType(): Int {
        return _type
    }

    fun getSingleFileMaxSize(): Long {
        return _singleFileMaxSize
    }

    fun getMaxFileCount(): Int {
        return _maxFileCount
    }

    fun getHour(): Int {
        return _hour
    }

    fun getMinute(): Int {
        return _minute
    }
}

package com.proxy.service.logfile.info.manager

import com.proxy.service.logfile.info.constants.Constants

/**
 * @author: cangHX
 * @date: 2025/1/17 14:40
 * @desc:
 */
class Strategy {
    var _pkg: String = ""

    /**
     * 周期性自动刷磁盘的间隔时间
     * */
    var _flushEveryTime: Long = Constants.FLUSH_EVERY_TIME

    /**
     * 同步模式
     * */
    var _isSyncMode: Boolean = Constants.IS_SYNC_MODE

    /**
     * 保存路径
     * */
    var _dir: String = ""

    /**
     * 日志文件前后缀
     * */
    var _namePrefix: String = Constants.NAME_PREFIX
    var _namePostfix: String = Constants.NAME_POSTFIX

    /**
     * 缓存时长与清理任务执行间隔时长
     * */
    var _cacheTime: Long = Constants.CACHE_TIME
    var _cleanTaskIntervalTime: Long = Constants.CLEAN_TASK_INTERVAL_TIME

    /**
     * 日志记录类型
     * */
    var _type: Int = Constants.TYPE_DAILY

    /**
     * 单个文件最大长度与最大文件数量 [Constants.TYPE_ROTATING]
     * */
    var _singleFileMaxSize: Long = Constants.SINGLE_FILE_MAX_SIZE
    var _maxFileCount: Int = Constants.MAX_FILE_COUNT

    /**
     * 文件创建时间节点 [Constants.TYPE_DAILY]
     * */
    var _hour: Int = Constants.HOUR
    var _minute: Int = Constants.MINUTE

    /**
     * 加密密钥（hex 字符串，64字符 = 32字节）
     * */
    var _encryptionKey: String = ""

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

    fun getEncryptionKey(): String {
        return _encryptionKey
    }
}

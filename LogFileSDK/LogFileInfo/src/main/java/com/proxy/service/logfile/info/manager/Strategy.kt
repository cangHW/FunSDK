package com.proxy.service.logfile.info.manager

import com.proxy.service.logfile.info.constants.Constants

/**
 * @author: cangHX
 * @data: 2025/1/17 14:40
 * @desc:
 */
class Strategy {
    var _pkg: String = ""

    var _isSyncMode: Boolean = false

    var _dir: String = ""

    var _namePrefix: String = ""
    var _namePostfix: String = ""

    var _cacheTime: Long = 0

    var _type: Int = Constants.TYPE_NORMAL

    var _singleFileMaxSize: Long = 0
    var _maxFileCount: Int = 0

    var _hour: Int = 0
    var _minute: Int = 0

    fun getPackageName(): String {
        return _pkg
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

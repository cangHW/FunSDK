package com.proxy.service.logfile.info.constants

/**
 * @author: cangHX
 * @data: 2025/1/16 20:19
 * @desc:
 */
object Constants {

    const val TAG = "LogFile"

    const val TYPE_NORMAL = 0

    const val TYPE_ROTATING = 1

    const val TYPE_DAILY = 2


    const val IS_SYNC_MODE = false

    const val NAME_PREFIX = "log"

    const val NAME_POSTFIX = ".log"

    const val CACHE_TIME = 7 * 24 * 60 * 60 * 1000L

    const val MAX_FILE_SIZE = 5 * 1024 * 1024L
    const val MAX_FILES = 3

    const val HOUR = 0
    const val MINUTE = 0
}
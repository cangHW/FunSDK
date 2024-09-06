package com.proxy.service.core.framework.log

import android.util.Log

/**
 * @author: cangHX
 * @data: 2024/4/28 19:20
 * @desc:
 */
enum class LogPriority constructor(val level: Int) {

    DEBUG(Log.DEBUG),
    ERROR(Log.ERROR),
    INFO(Log.INFO),
    VERBOSE(Log.VERBOSE),
    WARN(Log.WARN);

    companion object {
        fun value(level: Int): LogPriority? {
            when (level) {
                DEBUG.level -> return DEBUG
                ERROR.level -> return ERROR
                INFO.level -> return INFO
                VERBOSE.level -> return VERBOSE
                WARN.level -> return WARN
            }
            return null
        }
    }

}
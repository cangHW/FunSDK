package com.proxy.service.logfile.info.manager

/**
 * @author: cangHX
 * @data: 2025/12/29 11:12
 * @desc:
 */
enum class LogLevelMode(val level: Int) {

    VERBOSE(0),

    DEBUG(1),

    INFO(2),

    WARNING(3),

    ERROR(4),

    FATAL(5),

    NONE(6);

}
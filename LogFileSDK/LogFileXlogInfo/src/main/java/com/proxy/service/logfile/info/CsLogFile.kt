package com.proxy.service.logfile.info

import com.proxy.service.logfile.info.config.LogConfig

/**
 * logfile 日志文件框架入口
 *
 * @author: cangHX
 * @date: 2025/1/16 19:41
 * @desc:
 */
object CsLogFile {

    private var config: LogConfig = LogConfig.builder().createNormalType()

    /**
     * 设置配置
     * */
    fun setConfig(config: LogConfig) {
        this.config = config
    }

    fun getLogConfig(): LogConfig {
        return config
    }

}
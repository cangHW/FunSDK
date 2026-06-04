package com.proxy.service.core.constants

/**
 * @author: cangHX
 * @date: 2024/9/23 20:42
 * @desc:
 */
object CoreConfig {

    const val TAG = "CoreFw_"

    /**
     * 服务tag前缀——配置初始化
     * */
    const val SERVICE_TAG_CONFIG_PREFIX = "cs_config"

    /**
     * 服务tag前缀——初始化
     * */
    const val SERVICE_TAG_APP_PREFIX = "cs_application"


    /**
     * 初始化环境
     * */
    var isDebug = false

    /**
     * 是否初始化完成
     * */
    var isFrameworkInitFinish = false


    /**
     * 日志文件库——配置初始化
     * */
    const val PRIORITY_CONFIG_LOG_FILE = -1000

    /**
     * 架构基础库——配置初始化
     * */
    const val PRIORITY_CONFIG_CORE_FRAMEWORK = -995

    /**
     * 线程库——配置初始化
     * */
    const val PRIORITY_CONFIG_THREAD_POOL = -990

    /**
     * Web容器Bridge库——配置初始化
     * */
    const val PRIORITY_CONFIG_WEB_BRIDGE = -550

    /**
     * Web容器性能检测库——配置初始化
     * */
    const val PRIORITY_CONFIG_WEB_MONITOR = -550


    /**
     * 日志文件库——初始化
     * */
    const val PRIORITY_APP_LOG_FILE = -1000

    /**
     * 异常监控库——初始化
     * */
    const val PRIORITY_APP_APM = -995

    /**
     * Web容器库——初始化
     * */
    const val PRIORITY_APP_WEB = -550
}
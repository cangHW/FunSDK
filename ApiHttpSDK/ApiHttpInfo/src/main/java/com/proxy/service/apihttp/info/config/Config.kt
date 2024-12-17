package com.proxy.service.apihttp.info.config

/**
 * @author: cangHX
 * @data: 2024/5/21 18:09
 * @desc:
 */
object Config {

    /**
     * 下载任务中消息分发事件所在线程名称
     * */
    const val TASK_LOOP_THREAD_NAME = "download-dispatcher-thread"

    /**
     * 下载任务中默认组名称
     * */
    const val GROUP_DEFAULT_NAME = "download-group-name-default"

    /**
     * 配置文件名称
     * */
    const val CONFIG_FILE_NAME = "api_http_config"

    @Volatile
    private var isDebug = true

    fun setIsDebug(isDebug: Boolean) {
        Config.isDebug = isDebug
    }

    fun isDebug(): Boolean {
        return isDebug
    }
}
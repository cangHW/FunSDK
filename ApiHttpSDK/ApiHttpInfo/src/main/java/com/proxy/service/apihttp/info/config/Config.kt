package com.proxy.service.apihttp.info.config

/**
 * @author: cangHX
 * @data: 2024/5/21 18:09
 * @desc:
 */
object Config {

    @Volatile
    private var isDebug = true

    /*********  request  *********/


    /*********  download  *********/
    /**
     * 最大同时下载数量
     * */
    @Volatile
    private var maxTask: Int = 3


    fun setIsDebug(isDebug: Boolean) {
        Config.isDebug = isDebug
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    /*********  download  *********/
    fun setMaxDownloadTask(maxTask: Int) {
        this.maxTask = maxTask
    }

    fun getMaxDownloadTask(): Int {
        return maxTask
    }
}
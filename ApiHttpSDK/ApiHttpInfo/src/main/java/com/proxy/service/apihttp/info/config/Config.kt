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
    const val DOWNLOAD_DISPATCHER_THREAD_NAME = "download-dispatcher-thread"

    /**
     * 下载任务中默认组名称
     * */
    const val DOWNLOAD_DEFAULT_GROUP_NAME = "download-group-name-default"


    /**
     * 上传任务中消息分发事件所在线程名称
     * */
    const val UPLOAD_DISPATCHER_THREAD_NAME = "upload-dispatcher-thread"


    /**
     * 最大下载并发量
     * */
    var maxDownloadTaskCount = 3

    /**
     * 最大上传并发量
     * */
    var maxUploadTaskCount = 3

}
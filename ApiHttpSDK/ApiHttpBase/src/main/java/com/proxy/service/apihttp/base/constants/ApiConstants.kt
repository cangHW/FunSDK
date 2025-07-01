package com.proxy.service.apihttp.base.constants

/**
 * @author: cangHX
 * @data: 2024/11/1 11:43
 * @desc:
 */
object ApiConstants {

    private const val LOG_TAG_START = "ApiHttp_"

    /**
     * 接口请求日志前缀
     * */
    const val LOG_REQUEST_TAG_START = "${LOG_TAG_START}RQ_"

    /**
     * 下载任务日志前缀
     * */
    const val LOG_DOWNLOAD_TAG_START = "${LOG_TAG_START}DL_"

    /**
     * 上传任务日志前缀
     * */
    const val LOG_UPLOAD_TAG_START = "${LOG_TAG_START}UL_"

    /**
     * 下载相关常量
     * */
    object Download {

        /**
         * 每个分片的大小
         * */
        const val FILE_PART_SIZE = 80 * 1024 * 1024L

        /**
         * 最小分片数量
         * */
        const val MIN_PART_NUM = 3
    }

}
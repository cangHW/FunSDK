package com.proxy.service.apihttp.base.constants

/**
 * @author: cangHX
 * @date: 2024/11/1 11:43
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

        const val MAX_TASK_NUM = 3

        /**
         * 默认总长度
         * */
        const val TOTAL_FILE_SIZE = 0L

        /**
         * 每个分片的大小
         * */
        const val FILE_PART_SIZE = 5 * 1024 * 1024L

        /**
         * 最小分片数量
         * */
        const val MIN_PART_NUM = 3
    }

    /**
     * 连接、写入、读取最小超时时间
     * */
    const val DEFAULT_TIMEOUT_MIN: Long = 5 * 1000

    /**
     * 默认是否允许 http 重定向
     * */
    const val DEFAULT_FOLLOW_REDIRECTS = true

    /**
     * 默认是否允许 https 重定向
     * */
    const val DEFAULT_FOLLOW_PROTOCOL_REDIRECTS = true

}
package com.proxy.service.apihttp.base.common

/**
 * @author: cangHX
 * @data: 2025/3/24 15:08
 * @desc:
 */
class DownloadException private constructor(
    private val errorCode: Int = UNKNOWN,
    private val errorMsg: String
) : Exception(errorMsg) {

    /**
     * 错误码
     * */
    fun getErrorCode(): Int {
        return errorCode
    }

    /**
     * 错误信息
     * */
    fun getErrorMsg(): String {
        return errorMsg
    }

    override fun toString(): String {
        return "DownloadException(errorCode=$errorCode, errorMsg='$errorMsg')"
    }

    companion object {

        fun create(errorCode: Int, errorMsg: String?): DownloadException {
            return DownloadException(errorCode, errorMsg ?: "未知异常")
        }

        fun createUnknownError(errorMsg: String?): DownloadException {
            return create(UNKNOWN, errorMsg)
        }

        /**
         * 未知异常
         * */
        const val UNKNOWN = -1

        /**
         * 链接不能为空
         * */
        const val URL_IS_NULL = 1000

        /**
         * 网络异常, 无网络或网络不稳定
         * */
        const val NETWORK_ERROR = 1005

        /**
         * 找不到目标主机或 ip 地址
         * */
        const val UNKNOWN_HOST = 1010

        /**
         * 连接超时
         * */
        const val SOCKET_TIME_OUT = 1015

        /**
         * 创建文件失败
         * */
        const val CREATE_FILE_FAILURE = 1050

        /**
         * 存储空间不足
         * */
        const val INSUFFICIENT_STORAGE_SPACE = 1055


        /**
         * 文件写入失败
         * */
        const val FILE_WRITE_FAILURE = 1100

        /**
         * 文件找不到
         * */
        const val FILE_NOT_FOUND = 1105

        /**
         * 部分或全部失败
         * */
        const val MULTI_TASK_HAS_FAILURE = 1110


        /**
         * 文件合并失败
         * */
        const val FILE_MERGE_FAILURE = 1200

        /**
         * 文件长度不一致
         * */
        const val FILE_LENGTH_IS_INCONSISTENT = 1205

        /**
         * 文件 md5 不一致
         * */
        const val FILE_MD5_IS_INCONSISTENT = 1210

        /**
         * 文件重命名失败
         * */
        const val FILE_RENAME_FAILURE = 1215

    }

}
package com.proxy.service.logfile.info.manager

class LogFileDecompress private constructor() {

    companion object {
        init {
            System.loadLibrary("logfile")
        }

        private val _instance by lazy { LogFileDecompress() }

        fun getInstance(): LogFileDecompress {
            return _instance
        }
    }

    /**
     * 解压缩日志文件
     *
     * @param inputFilePath     压缩文件路径
     * @param outputFilePath    解压缩输出文件路径
     * @param encryptionKey     加密密钥
     * @return 解压缩是否成功
     */
    external fun decompressLogFile(
        inputFilePath: String,
        outputFilePath: String,
        encryptionKey: String
    ): Boolean

    /**
     * 批量解压缩目录中的日志文件
     *
     * @param inputDirPath  压缩文件目录
     * @param outputDirPath 解压缩输出目录
     * @param encryptionKey 加密密钥
     * @return 批量解压缩是否成功
     */
    external fun decompressLogDirectory(
        inputDirPath: String,
        outputDirPath: String,
        encryptionKey: String
    ): Boolean
}

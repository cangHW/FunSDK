package com.proxy.service.logfile.info.manager

/**
 * @author: cangHX
 * @data: 2025/10/22 17:19
 * @desc:
 */
enum class CompressionMode(val mode: String) {

    NONE(""),

    LZ4("lz4"),

    ZSTD("zstd");

}
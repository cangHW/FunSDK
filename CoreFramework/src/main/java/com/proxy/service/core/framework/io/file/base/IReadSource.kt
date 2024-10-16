package com.proxy.service.core.framework.io.file.base

import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 * @author: cangHX
 * @data: 2024/9/24 10:03
 * @desc:
 */
interface IReadSource {

    /**
     * 设置源数据
     *
     * fileName 示例：asd/xxx.txt
     * */
    fun setSourceAssetPath(fileName: String): IRead

    /**
     * 设置源数据
     * */
    fun setSourcePath(file: String): IRead

    /**
     * 设置源数据
     * */
    fun setSourceFile(file: File): IRead

    /**
     * 设置源数据
     * */
    fun setSourceStream(inputStream: InputStream): IRead

    /**
     * 设置源数据
     * */
    fun setSourceReader(reader: Reader): IRead
}
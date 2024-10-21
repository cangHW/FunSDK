package com.proxy.service.core.framework.io.file.base

import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 * @author: cangHX
 * @data: 2024/9/24 10:03
 * @desc:
 */
interface IWriteSource {

    /**
     * 设置源数据
     * */
    fun setSourceString(data: String): IWrite

    /**
     * 设置源数据
     * */
    fun setSourceByte(bytes: ByteArray): IWrite

    /**
     * 设置源数据
     *
     * fileName 示例：asd/xxx.txt
     * */
    fun setSourceAssetPath(fileName: String): IWrite

    /**
     * 设置源数据
     * */
    fun setSourcePath(filePath: String): IWrite

    /**
     * 设置源数据
     * */
    fun setSourceFile(file: File): IWrite

    /**
     * 设置源数据
     * */
    fun setSourceStream(inputStream: InputStream): IWrite

    /**
     * 设置源数据
     * */
    fun setSourceReader(reader: Reader): IWrite
}
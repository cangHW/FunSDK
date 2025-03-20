package com.proxy.service.core.framework.io.file.base

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.nio.file.Path

/**
 * @author: cangHX
 * @data: 2024/12/31 17:04
 * @desc:
 */
interface ISource<T> {

    /**
     * 设置源数据
     *
     * fileName 示例：asd/xxx.txt
     * */
    fun setSourceAssetPath(fileName: String): T

    /**
     * 设置源数据
     * */
    fun setSourcePath(filePath: String): T

    /**
     * 设置源数据
     * */
    fun setSourceFile(file: File): T

    /**
     * 设置源数据
     * */
    fun setSourcePath(path: Path): T

    /**
     * 设置源数据
     * */
    fun setSourceStream(inputStream: InputStream): T

    /**
     * 设置源数据
     * */
    fun setSourceReader(reader: Reader): T

}
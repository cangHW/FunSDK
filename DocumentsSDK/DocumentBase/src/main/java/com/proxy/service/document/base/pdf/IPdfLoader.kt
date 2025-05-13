package com.proxy.service.document.base.pdf

import com.proxy.service.document.base.config.pdf.callback.LoadStateCallback
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/4/29 22:25
 * @desc:
 */
interface IPdfLoader : IPdfRender {

    /**
     * 添加源数据
     *
     * assetPath 示例：asd/xxx.txt
     * */
    fun addSourceAssetPath(
        assetPath: String,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceFilePath(
        filePath: String,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceFile(
        file: File,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceData(
        bytes: ByteArray,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     *
     * assetPath 示例：asd/xxx.txt
     * */
    fun addSourceAssetPath(
        index: Int,
        assetPath: String,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceFilePath(
        index: Int,
        filePath: String,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceFile(
        index: Int,
        file: File,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceData(
        index: Int,
        bytes: ByteArray,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 关闭文档
     * */
    fun destroy()
}
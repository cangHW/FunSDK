package com.proxy.service.document.base.pdf.loader

import android.net.Uri
import com.proxy.service.document.base.pdf.config.callback.LoadStateCallback
import java.io.File
import java.io.InputStream

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
        filePath: String,
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
        file: File,
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
    fun addSourceByteArray(
        bytes: ByteArray,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 添加源数据
     * */
    fun addSourceByteArray(
        index: Int,
        bytes: ByteArray,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 设置源数据
     * */
    fun addSourceInputStream(
        inputStream: InputStream,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 设置源数据
     * */
    fun addSourceInputStream(
        index: Int,
        inputStream: InputStream,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 设置源数据
     * */
    fun addSourceUri(
        uri: Uri,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 设置源数据
     * */
    fun addSourceUri(
        index: Int,
        uri: Uri,
        password: String? = null,
        callback: LoadStateCallback? = null
    )

    /**
     * 关闭文档
     * */
    fun destroy()
}
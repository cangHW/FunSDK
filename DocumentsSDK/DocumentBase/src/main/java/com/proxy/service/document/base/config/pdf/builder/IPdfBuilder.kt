package com.proxy.service.document.base.config.pdf.builder

import com.proxy.service.document.base.config.pdf.PdfConfig
import com.proxy.service.document.base.config.pdf.callback.LoadStateCallback
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/4/29 22:05
 * @desc:
 */
interface IPdfBuilder {

    /**
     * 设置源数据
     *
     * assetPath 示例：asd/xxx.txt
     *
     * @param password  密码
     * */
    fun setSourceAssetPath(assetPath: String, password: String? = null): IPdfBuilder

    /**
     * 设置源数据
     *
     * @param password  密码
     * */
    fun setSourceFilePath(filePath: String, password: String? = null): IPdfBuilder

    /**
     * 设置源数据
     *
     * @param password  密码
     * */
    fun setSourceFile(file: File, password: String? = null): IPdfBuilder

    /**
     * 设置源数据
     *
     * @param password  密码
     * */
    fun setSourceByteArray(bytes: ByteArray, password: String? = null): IPdfBuilder

    /**
     * 添加源数据
     *
     * assetPath 示例：asd/xxx.txt
     *
     * @param password  密码
     * */
    fun addSourceAssetPath(assetPath: String, password: String? = null): IPdfBuilder

    /**
     * 添加源数据
     *
     * @param password  密码
     * */
    fun addSourceFilePath(filePath: String, password: String? = null): IPdfBuilder

    /**
     * 添加源数据
     *
     * @param password  密码
     * */
    fun addSourceFile(file: File, password: String? = null): IPdfBuilder

    /**
     * 添加源数据
     *
     * @param password  密码
     * */
    fun addSourceByteArray(bytes: ByteArray, password: String? = null): IPdfBuilder

    /**
     * 设置加载回调
     * */
    fun setLoadCallback(callback: LoadStateCallback): IPdfBuilder

    /**
     * 创建配置
     * */
    fun build(): PdfConfig
}
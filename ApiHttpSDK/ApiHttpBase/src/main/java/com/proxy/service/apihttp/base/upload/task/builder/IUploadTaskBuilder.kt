package com.proxy.service.apihttp.base.upload.task.builder

import com.proxy.service.apihttp.base.upload.task.UploadTask
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/12/17 18:49
 * @desc:
 */
interface IUploadTaskBuilder {

    /**
     * 添加 header
     * */
    fun addHeader(name: String, value: String): IUploadTaskBuilder

    /**
     * 添加附带参数
     * */
    fun addFormDataPart(name: String, value: String): IUploadTaskBuilder

    /**
     * 添加待上传文件
     * */
    fun addFormDataPart(name: String, file: File, fileName: String = file.name): IUploadTaskBuilder

    /**
     * 创建配置
     * */
    fun build(): UploadTask
}
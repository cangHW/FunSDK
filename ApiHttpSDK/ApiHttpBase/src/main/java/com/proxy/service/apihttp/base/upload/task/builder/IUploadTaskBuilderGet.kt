package com.proxy.service.apihttp.base.upload.task.builder

import com.proxy.service.apihttp.base.upload.task.FormDataPart
import com.proxy.service.apihttp.base.upload.task.UploadTask
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/12/17 18:49
 * @desc:
 */
interface IUploadTaskBuilderGet {

    /**
     * 获取任务唯一标识
     * */
    fun getTaskTag(): String

    /**
     * 获取上传链接
     * */
    fun getUrl(): String

    /**
     * 获取 header
     * */
    fun getHeaders(): HashMap<String, String>

    /**
     * 获取待上传信息
     * */
    fun getFormDataPart(): MutableList<FormDataPart>

}
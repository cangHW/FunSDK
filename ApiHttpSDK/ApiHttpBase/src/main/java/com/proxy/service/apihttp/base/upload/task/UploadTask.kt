package com.proxy.service.apihttp.base.upload.task

import com.proxy.service.apihttp.base.upload.task.builder.IUploadTaskBuilder
import com.proxy.service.apihttp.base.upload.task.builder.IUploadTaskBuilderGet
import com.proxy.service.core.framework.system.security.md5.CsMd5Utils
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/12/17 18:49
 * @desc:
 */
class UploadTask private constructor(
    private val builder: IUploadTaskBuilderGet
) : IUploadTaskBuilderGet {

    override fun getTaskTag(): String {
        return builder.getTaskTag()
    }

    override fun getUrl(): String {
        return builder.getUrl()
    }

    override fun getHeaders(): HashMap<String, String> {
        return builder.getHeaders()
    }

    override fun getFormDataPart(): MutableList<FormDataPart> {
        return builder.getFormDataPart()
    }

    companion object {

        /**
         * 创建构建器
         *
         * @param url 上传链接
         * */
        fun builder(url: String): IUploadTaskBuilder {
            return Builder(url)
        }
    }

    private class Builder(private val url: String) : IUploadTaskBuilder, IUploadTaskBuilderGet {

        companion object {
            private val count = AtomicInteger(0)
        }

        private var taskTag = ""
        private val header = HashMap<String, String>()
        private val formDataPart = ArrayList<FormDataPart>()

        override fun addHeader(name: String, value: String): IUploadTaskBuilder {
            header.put(name, value)
            return this
        }

        override fun addFormDataPart(name: String, value: String): IUploadTaskBuilder {
            formDataPart.add(FormDataPart.create(name, value))
            return this
        }

        override fun addFormDataPart(
            name: String,
            file: File,
            fileName: String
        ): IUploadTaskBuilder {
            formDataPart.add(FormDataPart.create(name, fileName, file))
            return this
        }

        override fun build(): UploadTask {
            taskTag = CsMd5Utils.getMD5("${url}_${count.getAndIncrement()}")
            return UploadTask(this)
        }

        override fun getTaskTag(): String {
            return taskTag
        }

        override fun getUrl(): String {
            return url
        }

        override fun getHeaders(): HashMap<String, String> {
            return header
        }

        override fun getFormDataPart(): MutableList<FormDataPart> {
            return formDataPart
        }
    }

}
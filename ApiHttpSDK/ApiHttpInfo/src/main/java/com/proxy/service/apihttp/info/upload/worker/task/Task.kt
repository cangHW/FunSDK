package com.proxy.service.apihttp.info.upload.worker.task

import com.proxy.service.apihttp.base.upload.task.FormDataPart
import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.upload.manager.OkhttpManager
import com.proxy.service.apihttp.info.upload.worker.body.ProgressRequestBody
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response


/**
 * @author: cangHX
 * @data: 2024/12/19 20:24
 * @desc:
 */
object Task {

    fun start(task: UploadTask, callback: (Call) -> Unit): Response {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        task.getFormDataPart().forEach { data ->
            if (data.type == FormDataPart.TYPE_TXT) {
                builder.addFormDataPart(data.name, data.value ?: "")
            } else if (data.type == FormDataPart.TYPE_STREAM) {
                data.file?.let { file ->
                    val fileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val progressBody = ProgressRequestBody(fileBody)
                    builder.addFormDataPart(data.name, data.fileName, progressBody)
                }
            }
        }
        val body = ProgressRequestBody(builder.build())

        val request = Request.Builder()
            .url(task.getUrl())
            .post(body)
            .build()

        val call = OkhttpManager.getOkhttpClient().newCall(request)
        callback(call)
        return call.execute()
    }

}
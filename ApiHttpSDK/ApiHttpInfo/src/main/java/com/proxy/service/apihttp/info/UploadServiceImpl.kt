package com.proxy.service.apihttp.info

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.base.upload.UploadService
import com.proxy.service.apihttp.base.upload.callback.UploadCallback
import com.proxy.service.apihttp.base.upload.config.UploadConfig
import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.upload.controller.TaskController
import com.proxy.service.apihttp.info.upload.manager.CallbackManager
import com.proxy.service.apihttp.info.upload.manager.impl.OkhttpConfigImpl


/**
 * @author: cangHX
 * @data: 2024/12/17 14:22
 * @desc:
 */
@CloudApiService(serviceTag = "service/http_upload")
class UploadServiceImpl : UploadService {

    override fun init(config: UploadConfig) {
        Config.maxUploadTaskCount = config.getMaxTask()
        OkhttpConfigImpl.instance.setUploadConfig(config)
    }

    override fun registerGlobalUploadCallback(callback: UploadCallback) {
        CallbackManager.addGlobalUploadCallback(callback)
    }

    override fun unregisterGlobalUploadCallback(callback: UploadCallback) {
        CallbackManager.removeGlobalUploadCallback(callback)
    }

    override fun addTask(
        task: UploadTask,
        callback: UploadCallback?,
        lifecycleOwner: LifecycleOwner?
    ): String {
        callback?.let {
            CallbackManager.addTaskUploadCallback(task.getTaskTag(), it, lifecycleOwner)
        }
        TaskController.addTask(task)
        return task.getTaskTag()
    }

    override fun removeTaskUploadCallback(callback: UploadCallback?) {
        callback?.let {
            CallbackManager.removeTaskUploadCallback(it)
        }
    }

    override fun cancel(taskTag: String) {
        TaskController.cancel(taskTag)
    }

    override fun cancelAllTask() {
        TaskController.cancelAll()
    }

}
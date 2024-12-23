package com.proxy.service.apihttp.base.upload

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.apihttp.base.upload.callback.UploadCallback
import com.proxy.service.apihttp.base.upload.config.UploadConfig
import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.base.BaseService

/**
 * @author: cangHX
 * @data: 2024/5/21 10:26
 * @desc:
 */
interface UploadService : BaseService {

    /**
     * 初始化
     * */
    fun init(config: UploadConfig)

    /**
     * 设置全局上传回调, 可用于监听全部任务
     * */
    fun registerGlobalUploadCallback(callback: UploadCallback)

    /**
     * 移除全局上传回调
     * */
    fun unregisterGlobalUploadCallback(callback: UploadCallback)

    /**
     * 添加上传任务
     *
     *  @param task      上传任务信息
     *  @param callback  上传回调
     *  @param lifecycleOwner    绑定生命周期, 用于在合适的时机进行回调以及移除对应回调
     *
     *  @return 当前任务 taskTag
     * */
    fun addTask(
        task: UploadTask,
        callback: UploadCallback? = null,
        lifecycleOwner: LifecycleOwner? = null
    ): String

    /**
     * 移除上传任务回调
     * */
    fun removeTaskUploadCallback(callback: UploadCallback?)

    /**
     * 取消任务,
     * */
    fun cancel(taskTag: String)

    /**
     * 取消全部任务
     * */
    fun cancelAllTask()
}
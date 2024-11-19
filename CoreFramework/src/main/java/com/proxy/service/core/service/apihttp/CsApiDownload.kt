package com.proxy.service.core.service.apihttp

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.api.CloudSystem
import com.proxy.service.apihttp.base.download.DownloadService
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * http 下载框架入口
 *
 * @author: cangHX
 * @data: 2024/11/5 13:38
 * @desc:
 */
object CsApiDownload {

    private const val TAG = "${Constants.TAG}ApiDownload"

    private var service: DownloadService? = null

    private fun getService(): DownloadService? {
        if (service == null) {
            service = CloudSystem.getService(DownloadService::class.java)
        }
        if (service == null) {
            CsLogger.tag(TAG)
                .e("Please check to see if it is referenced. <io.github.cangHW:Service-Apihttp:xxx>")
        }
        return service
    }

    private var config: DownloadConfig? = DownloadConfig.builder().build()

    /**
     * 下载库初始化
     * */
    fun init(config: DownloadConfig) {
        this.config = config
    }

    /**
     * 设置全局下载回调, 可用于监听全部任务
     * */
    fun registerGlobalDownloadCallback(callback: DownloadCallback) {
        getService()?.registerGlobalDownloadCallback(callback)
    }

    /**
     * 移除全局下载回调
     * */
    fun unregisterGlobalDownloadCallback(callback: DownloadCallback) {
        getService()?.unregisterGlobalDownloadCallback(callback)
    }

    /**
     * 添加下载任务
     *
     * @return 返回当前任务唯一标识, 用于后续操作
     * */
    fun addTask(
        task: DownloadTask,
        callback: DownloadCallback? = null,
        lifecycleOwner: LifecycleOwner? = null
    ): String {
        config?.let {
            val loader = getService()
            loader?.init(it)
            return loader?.addTask(task, callback, lifecycleOwner) ?: ""
        } ?: let {
            CsLogger.tag(TAG).e("Please check whether initialization has been performed.")
        }
        return ""
    }

    /**
     * 移除下载任务回调
     * */
    fun removeTaskDownloadCallback(callback: DownloadCallback?) {
        getService()?.removeTaskDownloadCallback(callback)
    }

    /**
     * 可用于重新开始一个已记录但不在任务队列中的任务, 例如：已失败、已取消、已完成等状态的任务
     *
     * @return 是否重新开始成功
     * */
    fun reStartTask(taskTag: String): Boolean {
        return getService()?.reStartTask(taskTag) ?: false
    }

    /**
     * 重置并还原正在运行的任务到未运行状态, 可用于让高优先级任务立刻执行
     * */
    fun resetRunningTask() {
        getService()?.resetRunningTask()
    }

    /**
     * 获取任务
     * */
    fun getTask(taskTag: String): DownloadTask? {
        return getService()?.getTask(taskTag)
    }

    /**
     * 获取下载状态
     * */
    fun getDownloadStatus(taskTag: String): StatusEnum {
        val status = getService()?.getDownloadStatus(taskTag)
        if (status != null) {
            return status
        }
        return StatusEnum.UNKNOWN
    }

    /**
     * 取消任务,
     * */
    fun cancel(taskTag: String) {
        getService()?.cancel(taskTag)
    }

    /**
     * 取消任务组内全部任务
     * */
    fun cancelGroup(groupName: String) {
        getService()?.cancelGroup(groupName)
    }

    /**
     * 取消全部任务
     * */
    fun cancelAllTask() {
        getService()?.cancelAllTask()
    }

}
package com.proxy.service.apihttp.base.download

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.base.BaseService

/**
 * 下载服务
 *
 * @author: cangHX
 * @data: 2024/5/21 10:26
 * @desc:
 */
interface DownloadService : BaseService {

    fun xx(){
       val builder = DownloadConfig.builder().build()
        init(builder)
    }

    /**
     * 初始化
     * */
    fun init(config: DownloadConfig)

    /**
     * 设置全局下载回调, 可用于监听全部任务
     * */
    fun registerGlobalDownloadCallback(callback: DownloadCallback)

    /**
     * 移除全局下载回调
     * */
    fun unregisterGlobalDownloadCallback(callback: DownloadCallback)

    /**
     * 添加下载任务
     *
     * @param task      下载任务信息
     * @param callback  下载回调
     * @param lifecycleOwner    绑定生命周期, 用于在合适的时机进行回调以及移除对应回调
     *
     * @return 当前任务 taskTag
     * */
    fun addTask(
        task: DownloadTask,
        callback: DownloadCallback? = null,
        lifecycleOwner: LifecycleOwner? = null
    ): String

    /**
     * 移除下载任务回调
     * */
    fun removeTaskDownloadCallback(callback: DownloadCallback?)

    /**
     * 可用于重新开始一个已记录但不在任务队列中的任务, 例如：已失败、已取消、已完成等状态的任务
     *
     * @return 是否重新开始成功
     * */
    fun reStartTask(taskTag: String): Boolean

    /**
     * 重置并还原正在运行的任务到未运行状态, 可用于让高优先级任务立刻执行
     * */
    fun resetRunningTask()

    /**
     * 获取任务
     * */
    fun getTask(taskTag: String): DownloadTask?

    /**
     * 获取下载状态, 不校验文件 md5
     * */
    fun getDownloadStatus(taskTag: String): StatusEnum

    /**
     * 取消任务
     * */
    fun cancel(taskTag: String)

    /**
     * 取消任务组内全部任务
     * */
    fun cancelGroup(groupName: String)

    /**
     * 取消全部任务
     * */
    fun cancelAllTask()
}
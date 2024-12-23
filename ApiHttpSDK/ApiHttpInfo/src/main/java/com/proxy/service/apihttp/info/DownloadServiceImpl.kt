package com.proxy.service.apihttp.info

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.DownloadService
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.config.DownloadConfig
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.download.controller.TaskController
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.apihttp.info.download.dispatcher.TaskDispatcher
import com.proxy.service.apihttp.info.download.manager.AppRelaunchManager
import com.proxy.service.apihttp.info.download.manager.CallbackManager
import com.proxy.service.apihttp.info.download.manager.NetworkManager
import com.proxy.service.apihttp.info.download.manager.impl.OkhttpConfigImpl
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/10/31 11:06
 * @desc:
 */
@CloudApiService(serviceTag = "service/http_download")
class DownloadServiceImpl : DownloadService {

    private val tag = "${Constants.LOG_DOWNLOAD_TAG_START}Service"

    private val lock = Any()

    @Volatile
    private var isInit = false

    override fun init(config: DownloadConfig) {
        if (!isInit) {
            synchronized(lock) {
                if (!isInit) {
                    OkhttpConfigImpl.instance.setDownloadConfig(config)
                    TaskController.addGroup(config.getGroups())
                    Config.maxDownloadTaskCount = config.getMaxTask()
                    AppRelaunchManager.reResumeTask(config.getAutoResumeOnAppRelaunch())
                    NetworkManager.reStartTask(config.getAutoRestartOnNetworkReconnect())
                    isInit = true
                }
            }
        }
    }

    override fun registerGlobalDownloadCallback(callback: DownloadCallback) {
        CallbackManager.addGlobalDownloadCallback(callback)
    }

    override fun unregisterGlobalDownloadCallback(callback: DownloadCallback) {
        CallbackManager.removeGlobalDownloadCallback(callback)
    }

    override fun addTask(
        task: DownloadTask,
        callback: DownloadCallback?,
        lifecycleOwner: LifecycleOwner?
    ): String {
        if (task.getDownloadUrl().trim().isEmpty()) {
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    callback?.onStart(task)
                    callback?.onFailed(
                        task,
                        IllegalArgumentException("downloadUrl 不能为空. downloadUrl = ${task.getDownloadUrl()}")
                    )
                    return ""
                }
            })?.start()
            return task.getTaskTag()
        }
        callback?.let {
            CallbackManager.addTaskDownloadCallback(task.getTaskTag(), it, lifecycleOwner)
        }
        TaskController.addTask(task)
        return task.getTaskTag()
    }

    override fun removeTaskDownloadCallback(callback: DownloadCallback?) {
        callback?.let {
            CallbackManager.removeTaskDownloadCallback(it)
        }
    }

    override fun reStartTask(taskTag: String): Boolean {
        val task = DownloadRoom.INSTANCE.getTaskDao().query(taskTag)?.getDownloadTask()
        if (task == null) {
            CsLogger.tag(tag).e("该任务未记录, taskTag = $taskTag")
            return false
        }
        TaskController.addTask(task)
        return true
    }

    override fun resetRunningTask() {
        TaskDispatcher.resetAllRunningTask()
    }

    override fun getTask(taskTag: String): DownloadTask? {
        return TaskController.getTask(taskTag)
    }

    override fun getDownloadStatus(taskTag: String): StatusEnum {
        val status = DownloadRoom.INSTANCE.getTaskDao().queryStatus(taskTag)
        var statusEnum = StatusEnum.value(status)
        if (statusEnum == StatusEnum.UNKNOWN) {
            CsLogger.tag(tag).e("该任务未与下载器完成绑定, taskTag = $taskTag")
        }
        if (statusEnum == StatusEnum.SUCCESS) {
            TaskController.getTask(taskTag)?.let {
                val fileLength = CsFileUtils.length(it.getFilePath())
                if (fileLength <= 0) {
                    statusEnum = StatusEnum.FILE_DELETE
                } else if (fileLength != it.getFileSize()) {
                    statusEnum = StatusEnum.FAILED
                }
            }
        }
        return statusEnum
    }

    override fun cancel(taskTag: String) {
        TaskController.cancelTask(taskTag)
    }

    override fun cancelGroup(groupName: String) {
        TaskController.cancelTaskByGroup(groupName)
    }

    override fun cancelAllTask() {
        TaskController.cancelAllTask()
    }

}
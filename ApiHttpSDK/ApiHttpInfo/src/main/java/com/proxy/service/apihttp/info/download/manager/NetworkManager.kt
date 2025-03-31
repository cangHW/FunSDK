package com.proxy.service.apihttp.info.download.manager

import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.controller.TaskController
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.CsNetManager
import com.proxy.service.core.framework.system.net.callback.NetConnectChangedListener
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/15 16:56
 * @desc:
 */
object NetworkManager {

    private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}Network"

    private val lock = Any()
    private val failedTaskMap = ArrayList<DownloadTask>()

    fun reStartTask(isAutoRestartOnNetworkReconnect: Boolean) {
        if (!isAutoRestartOnNetworkReconnect) {
            return
        }
        CallbackManager.addGlobalDownloadCallback(downloadCallback)
        CsNetManager.addWeakNetConnectChangedListener(callback)
    }

    private val callback = object : NetConnectChangedListener {
        /**
         * 网络连接
         */
        override fun onNetConnected() {
            CsLogger.tag(TAG).d("onNetConnected")
            reLoadTask()
        }

        /**
         * 网络断开连接
         */
        override fun onNetDisConnected() {
            CsLogger.tag(TAG).d("onNetConnected")
        }
    }

    private fun reLoadTask() {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                while (failedTaskMap.size > 0) {
                    val task = synchronized(lock) {
                        failedTaskMap.removeLastOrNull()
                    }
                    task?.let {
                        TaskController.addTask(it)
                    }
                }
                return ""
            }
        })?.start()
    }

    private val downloadCallback = object : DownloadCallback {
        override fun onWaiting(task: DownloadTask) {

        }

        override fun onStart(task: DownloadTask) {

        }

        override fun onProgress(
            task: DownloadTask,
            currentSize: Long,
            progress: Float,
            speed: Long
        ) {

        }

        override fun onSuccess(task: DownloadTask) {

        }

        override fun onCancel(task: DownloadTask) {

        }

        override fun onFailed(task: DownloadTask, exception: DownloadException) {
            synchronized(lock) {
                failedTaskMap.add(task)
            }
        }
    }
}
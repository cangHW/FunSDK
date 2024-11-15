package com.proxy.service.apihttp.info.download.manager

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.info.download.controller.TaskController
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.CsNetUtils
import com.proxy.service.core.framework.system.net.CsNetUtils.NetConnectChangedListener
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/15 16:56
 * @desc:
 */
object NetworkManager {

    private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}Network"

    fun reStartTask(isAutoRestartOnNetworkReconnect: Boolean) {
        if (!isAutoRestartOnNetworkReconnect) {
            return
        }
        CsNetUtils.addWeakNetConnectChangedListener(callback)
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
                DownloadRoom.INSTANCE.getTaskDao().queryTasksByStatus(StatusEnum.FAILED.status)
                    .forEach {
                        TaskController.addTask(it.getDownloadTask())
                    }
                return ""
            }
        })?.start()
    }
}
package com.proxy.service.core.framework.app.message.process.response

import com.proxy.service.core.framework.app.message.process.bean.MessageType
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.exceptions.NotSupportException
import com.proxy.service.core.framework.app.message.process.exceptions.SeverException
import com.proxy.service.core.framework.app.message.process.exceptions.TimeOutException
import com.proxy.service.core.framework.app.message.process.exceptions.UnInstallException
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/9/15 19:39
 * @desc:
 */
object ResponseDispatch {

    private const val LOOP_TASK_NAME = "share-data"
    private val handler = CsTask.launchTaskGroup(LOOP_TASK_NAME)
    private val taskParams = CsExcellentMap<String, TaskParams>()

    fun dispatch(request: ShareMessage): ShareMessage {
        refreshTask(request.messageId)
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                when (request.getMessageType()) {
                    MessageType.RESPONSE_ERROR -> {
                        callError(request.messageId, request.content.toIntOrNull())
                    }

                    MessageType.RESPONSE_PROGRESS -> {
                        taskParams.get(request.messageId)
                            ?.callback
                            ?.onProgress(
                                request.messageVersion,
                                request.messageTime,
                                request.method,
                                request.content
                            )
                    }

                    MessageType.RESPONSE_FINISH -> {
                        taskParams.removeSync(request.messageId)?.let {
                            cancelTask(it.messageId)
                            it.callback.onSuccess(
                                request.messageVersion,
                                request.messageTime,
                                request.method,
                                request.content
                            )
                        }
                    }

                    else -> {}
                }
                return ""
            }
        })?.start()
        return ShareMessageFactory.createResponseFinish(request.messageVersion, request, "")
    }

    fun addWaitingTask(
        request: ShareMessage,
        toPkg: String,
        timeout: Long,
        callback: RequestCallback?
    ) {
        if (callback == null) {
            return
        }
        val params = TaskParams(request.messageId, toPkg, request.method, timeout, callback)
        taskParams.putSync(request.messageId, params)
        startTask(request.messageId, timeout)
    }

    private fun refreshTask(messageId: String) {
        cancelTask(messageId)
        taskParams.get(messageId)?.let {
            startTask(it.messageId, it.timeout)
        }
    }

    private fun cancelTask(messageId: String) {
        handler?.clearAllTaskWithTag(messageId)
    }

    private fun startTask(messageId: String, timeout: Long) {
        handler?.setDelay(timeout, TimeUnit.MILLISECONDS)?.start(messageId) {
            callError(messageId, RequestCallback.ERROR_CODE_TIME_OUT)
        }
    }

    private fun callError(messageId: String, errorCode: Int?) {
        cancelTask(messageId)
        val params = taskParams.removeSync(messageId) ?: return
        if (errorCode == null) {
            return
        }

        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                when (errorCode) {
                    RequestCallback.ERROR_CODE_UNINSTALL -> {
                        params.callback.onFailed(
                            RequestCallback.ERROR_CODE_UNINSTALL,
                            UnInstallException(
                                RequestCallback.ERROR_CODE_UNINSTALL,
                                "The target app is not installed. package: ${params.toPkg}"
                            )
                        )
                    }

                    RequestCallback.ERROR_CODE_TIME_OUT -> {
                        params.callback.onFailed(
                            RequestCallback.ERROR_CODE_TIME_OUT,
                            TimeOutException(
                                RequestCallback.ERROR_CODE_TIME_OUT,
                                "There is still no result after the waiting time, which is ${params.timeout} milliseconds"
                            )
                        )
                    }

                    RequestCallback.ERROR_METHOD_NOT_SUPPORT -> {
                        params.callback.onFailed(
                            RequestCallback.ERROR_METHOD_NOT_SUPPORT,
                            NotSupportException(
                                RequestCallback.ERROR_METHOD_NOT_SUPPORT,
                                "The current method is not supported. method: ${params.method}"
                            )
                        )
                    }

                    RequestCallback.ERROR_CODE_SEVER_ERROR -> {
                        params.callback.onFailed(
                            RequestCallback.ERROR_CODE_SEVER_ERROR,
                            SeverException(
                                RequestCallback.ERROR_CODE_SEVER_ERROR,
                                "An exception occurred on the server side."
                            )
                        )
                    }
                }
                return ""
            }
        })?.start()
    }

}
package com.proxy.service.core.framework.app.message.process.response

import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.exceptions.NotSupportException
import com.proxy.service.core.framework.app.message.process.exceptions.SeverException
import com.proxy.service.core.framework.app.message.process.exceptions.TimeOutException
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
        CsTask.mainThread()
            ?.call(object : ICallable<String> {
                override fun accept(): String {
                    when (request.messageType) {
                        ShareMessageFactory.DEFAULT_TYPE_RESPONSE_ERROR -> {
                            taskParams.removeSync(request.messageId)?.let {
                                cancelTask(it.messageId)
                                val code = request.content.toIntOrNull()
                                if (code == RequestCallback.ERROR_METHOD_NOT_SUPPORT) {
                                    it.callback.onFailed(
                                        RequestCallback.ERROR_METHOD_NOT_SUPPORT,
                                        NotSupportException(
                                            RequestCallback.ERROR_METHOD_NOT_SUPPORT,
                                            "The current method is not supported. method: ${request.method}"
                                        )
                                    )
                                } else if (code == RequestCallback.ERROR_CODE_SEVER_ERROR) {
                                    it.callback.onFailed(
                                        RequestCallback.ERROR_CODE_SEVER_ERROR,
                                        SeverException(
                                            RequestCallback.ERROR_CODE_SEVER_ERROR,
                                            "An exception occurred on the server side."
                                        )
                                    )
                                }
                            }
                        }

                        ShareMessageFactory.DEFAULT_TYPE_RESPONSE_PROGRESS -> {
                            taskParams.get(request.messageId)?.callback?.onProgress(request)
                        }

                        ShareMessageFactory.DEFAULT_TYPE_RESPONSE_FINISH -> {
                            taskParams.removeSync(request.messageId)?.let {
                                cancelTask(it.messageId)
                                it.callback.onSuccess(request)
                            }
                        }

                        else -> {}
                    }
                    return ""
                }
            })
            ?.start()
        return ShareMessageFactory.createResponseFinish(
            request.messageVersion,
            request,
            ""
        )
    }

    fun addWaitingTask(messageId: String, timeout: Long, callback: RequestCallback?) {
        if (callback == null) {
            return
        }
        val params = TaskParams(messageId, timeout, callback)
        taskParams.putSync(messageId, params)
        startTask(messageId, timeout)
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
        handler?.setDelay(timeout, TimeUnit.MILLISECONDS)
            ?.start(messageId) {
                taskParams.removeSync(messageId)?.callback?.onFailed(
                    RequestCallback.ERROR_CODE_TIME_OUT,
                    TimeOutException(
                        RequestCallback.ERROR_CODE_TIME_OUT,
                        "There is still no result after the waiting time, which is $timeout milliseconds"
                    )
                )
            }
    }
}
package com.proxy.service.core.framework.app.message.process

import com.proxy.service.core.framework.app.message.broadcast.BroadcastReceiverImpl
import com.proxy.service.core.framework.app.message.process.bean.MessageType
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.channel.ChannelManager
import com.proxy.service.core.framework.app.message.process.channel.ReceiveChannel
import com.proxy.service.core.framework.app.message.process.channel.SendChannel
import com.proxy.service.core.framework.app.message.process.channel.provider.BroadcastFactory
import com.proxy.service.core.framework.app.message.process.channel.provider.ProviderFactory
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.request.RequestDispatch
import com.proxy.service.core.framework.app.message.process.response.ResponseDispatch
import com.proxy.service.core.framework.app.message.process.woker.AbstractAsyncWorker
import com.proxy.service.core.framework.app.message.process.woker.AbstractSyncWorker
import com.proxy.service.core.framework.app.message.process.woker.AbstractWorker
import com.proxy.service.core.framework.app.message.provider.ContentProviderImpl
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.TimeUnit

/**
 * 跨进程共享数据
 *
 * @author: cangHX
 * @data: 2025/9/17 18:53
 * @desc:
 */
class CsShareManager private constructor(private val toPkg: String, private val method: String) {

    companion object {
        private const val DEFAULT_VERSION: String = "V1"
        private const val DEFAULT_TIMEOUT = 10 * 1000L

        init {
            ContentProviderImpl.addReceiverListener(
                ShareDataConstants.SHARE_DATA_PROVIDER_METHOD_NAME,
                ProviderFactory.getInstance()
            )
            BroadcastReceiverImpl.addWeakReceiverListener(
                ShareDataConstants.SHARE_DATA_BROADCAST_ACTION_NAME,
                BroadcastFactory.getInstance()
            )
        }

        /**
         * 创建消息发送器
         * */
        fun create(toPkg: String, method: String): CsShareManager {
            return CsShareManager(toPkg, method)
        }

        /**
         * 添加同步任务处理器
         * */
        fun addWorker(worker: AbstractSyncWorker) {
            RequestDispatch.addWorker(worker)
        }

        /**
         * 添加异步任务处理器
         * */
        fun addWorker(worker: AbstractAsyncWorker) {
            RequestDispatch.addWorker(worker)
        }

        /**
         * 移除任务处理器
         * */
        fun removeWorker(worker: AbstractWorker) {
            RequestDispatch.removeWorker(worker)
        }
    }

    private var version = DEFAULT_VERSION
    private var timeout = DEFAULT_TIMEOUT
    private var params = ""
    private var sendChannel = SendChannel.AUTO
    private var receiveChannel = ReceiveChannel.AUTO

    /**
     * 设置版本号, 用于调用同一功能的不同版本, 需要 sever 端支持
     * */
    fun setVersion(version: String): CsShareManager {
        this.version = version
        return this
    }

    /**
     * 设置超时时间
     * */
    fun setTimeout(timeout: Long, unit: TimeUnit): CsShareManager {
        this.timeout = unit.toMillis(timeout)
        return this
    }

    /**
     * 设置请求参数
     * */
    fun setParams(params: String): CsShareManager {
        this.params = params
        return this
    }

    /**
     * 设置发送渠道, 默认: [SendChannel.AUTO]
     * */
    fun setSendChannel(channel: SendChannel): CsShareManager {
        this.sendChannel = channel
        return this
    }

    /**
     * 设置接收渠道, 默认: [ReceiveChannel.AUTO]
     * */
    fun setReceiveChannel(channel: ReceiveChannel): CsShareManager {
        this.receiveChannel = channel
        return this
    }

    /**
     * 同步执行
     * */
    fun execute(): ShareMessage? {
        CsLogger.tag(ShareDataConstants.TAG).d("CsShareManager execute")
        val response: ShareMessage? = CsTask.ioThread()?.call(object : ICallable<ShareMessage> {
            override fun accept(): ShareMessage {
                val message = ShareMessageFactory.createRequestSync(
                    version,
                    receiveChannel.name,
                    method,
                    params
                )
                val result = ChannelManager.send(toPkg, sendChannel.name, message)
                    ?: throw UnknownError("result is null")
                return result
            }
        })?.timeout(timeout, TimeUnit.MILLISECONDS)?.blockGetFirst()
        return response
    }

    /**
     * 异步执行
     * */
    fun enqueue(callback: RequestCallback?) {
        CsLogger.tag(ShareDataConstants.TAG).d("CsShareManager enqueue")
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val receiver = if (callback == null) {
                    ReceiveChannel.NONE
                } else {
                    receiveChannel
                }

                val request = ShareMessageFactory.createRequestAsync(
                    version,
                    receiver.name,
                    method,
                    params
                )
                if (receiver != ReceiveChannel.NONE) {
                    ResponseDispatch.addWaitingTask(request, toPkg, timeout, callback)
                }

                var result = ChannelManager.send(toPkg, sendChannel.name, request)
                if (result?.getMessageType() != MessageType.RESPONSE_WAITING) {
                    if (result == null || result.getMessageType() != MessageType.RESPONSE_ERROR) {
                        result = ShareMessageFactory.createResponseError(
                            request,
                            RequestCallback.ERROR_CODE_SEVER_ERROR.toString()
                        )
                    }
                    ResponseDispatch.dispatch(result)
                }
                return ""
            }
        })?.start()
    }

}
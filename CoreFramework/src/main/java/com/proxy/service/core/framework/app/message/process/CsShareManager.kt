package com.proxy.service.core.framework.app.message.process

import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.channel.ChannelEnum
import com.proxy.service.core.framework.app.message.process.channel.ChannelManager
import com.proxy.service.core.framework.app.message.process.channel.provider.ProviderFactory
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.exceptions.UnInstallException
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
class CsShareManager private constructor(
    private val toPkg: String,
    private val method: String
) {

    companion object {

        init {
            ContentProviderImpl.addReceiverListener(
                ShareDataConstants.SHARE_DATA_PROVIDER_METHOD_NAME,
                ProviderFactory.getInstance()
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

    private var version = ShareMessageFactory.DEFAULT_VERSION
    private var timeout: Long = 10 * 1000
    private var params = ""
    private var sendChannel = ChannelEnum.AUTO
    private var receiveChannel = ChannelEnum.AUTO

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
     * 设置发送渠道
     * */
    fun setSendChannel(channel: ChannelEnum): CsShareManager {
        this.sendChannel = channel
        return this
    }

    /**
     * 设置接收渠道
     * */
    fun setReceiveChannel(channel: ChannelEnum): CsShareManager {
        this.receiveChannel = channel
        return this
    }

    /**
     * 同步执行, 需要配置目标应用可见, 配置方式参考 [CsInstallUtils.isInstallApp]
     * */
    fun get(): ShareMessage? {
        val response: ShareMessage? = CsTask.ioThread()
            ?.call(object : ICallable<ShareMessage> {
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
            })
            ?.timeout(timeout, TimeUnit.MILLISECONDS)
            ?.blockGetFirst()
        return response
    }

    /**
     * 异步执行, 需要配置目标应用可见, 配置方式参考 [CsInstallUtils.isInstallApp]
     * */
    fun post(callback: RequestCallback?) {
        if (!CsInstallUtils.isInstallApp(toPkg)) {
            if (callback == null) {
                return
            }
            CsTask.mainThread()
                ?.call(object : ICallable<String> {
                    override fun accept(): String {
                        callback.onFailed(
                            RequestCallback.ERROR_CODE_UNINSTALL,
                            UnInstallException(
                                RequestCallback.ERROR_CODE_UNINSTALL,
                                "The target app is not installed. package: $toPkg"
                            )
                        )
                        return ""
                    }
                })
                ?.start()
            return
        }

        CsTask.ioThread()
            ?.call(object : ICallable<String> {
                override fun accept(): String {
                    val receiver = if (callback == null) {
                        ChannelEnum.NONE
                    } else {
                        receiveChannel
                    }

                    val request = ShareMessageFactory.createRequestAsync(
                        version,
                        receiver.name,
                        method,
                        params
                    )
                    if (receiver != ChannelEnum.NONE) {
                        ResponseDispatch.addWaitingTask(request.messageId, timeout, callback)
                    }
                    val result = ChannelManager.send(toPkg, sendChannel.name, request)
                    if (result == null) {
                        val errorMessage = ShareMessageFactory.createResponseError(
                            request,
                            RequestCallback.ERROR_CODE_SEVER_ERROR
                        )
                        ResponseDispatch.dispatch(errorMessage)
                    }
                    return ""
                }
            })
            ?.start()
    }

}
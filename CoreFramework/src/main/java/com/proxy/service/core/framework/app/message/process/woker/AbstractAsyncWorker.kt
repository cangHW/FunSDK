package com.proxy.service.core.framework.app.message.process.woker

import com.proxy.service.core.framework.app.message.process.bean.MessageType
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.channel.ChannelManager
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/9/15 23:03
 * @desc:
 */
abstract class AbstractAsyncWorker : AbstractWorker() {

    interface WorkerProgressCallback {
        /**
         * 回调进度
         *
         * @param result    回调数据
         * */
        fun onProgress(result: String): Boolean

        /**
         * 回调进度
         *
         * @param version   版本号
         * @param result    回调数据
         * */
        fun onProgress(version: String, result: String): Boolean

        /**
         * 回调结束
         *
         * @param result    回调数据
         * */
        fun onFinish(result: String): Boolean

        /**
         * 回调结束
         *
         * @param version   版本号
         * @param result    回调数据
         * */
        fun onFinish(version: String, result: String): Boolean
    }

    /**
     * 获取返回值
     * */
    final override fun getResponse(fromPkg: String, request: ShareMessage): ShareMessage {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                doWork(fromPkg, request, object : WorkerProgressCallback {
                    override fun onProgress(result: String): Boolean {
                        return onProgress(request.messageVersion, result)
                    }

                    override fun onProgress(version: String, result: String): Boolean {
                        val message = ShareMessageFactory.createResponseProgress(
                            version,
                            request,
                            result
                        )
                        val call = ChannelManager.send(fromPkg, request.receiveChannel, message)
                        if (call?.getMessageType() != MessageType.RESPONSE_FINISH) {
                            CsLogger.tag(ShareDataConstants.TAG)
                                .e("AbstractAsyncWorker send onProgress error. result=$call")
                            return false
                        }
                        return true
                    }

                    override fun onFinish(result: String): Boolean {
                        return onFinish(request.messageVersion, result)
                    }

                    override fun onFinish(version: String, result: String): Boolean {
                        val message = ShareMessageFactory.createResponseFinish(
                            version,
                            request,
                            result
                        )
                        val call = ChannelManager.send(fromPkg, request.receiveChannel, message)
                        if (call?.getMessageType() != MessageType.RESPONSE_FINISH) {
                            CsLogger.tag(ShareDataConstants.TAG)
                                .e("AbstractAsyncWorker send onProgress error. result=$call")
                            return false
                        }
                        return true
                    }
                })
                return ""
            }
        })?.start()
        return ShareMessageFactory.createResponseWaiting(request)
    }

    /**
     * 执行逻辑, 该方法执行时, application 已经完成初始化
     *
     * @param fromPkg   请求方包名
     * @param request   请求信息
     * */
    open fun doWork(fromPkg: String, request: ShareMessage, callback: WorkerProgressCallback) {
        doWork(request, callback)
    }

    /**
     * 执行逻辑, 该方法执行时, application 已经完成初始化
     *
     * @param request   请求信息
     * */
    abstract fun doWork(request: ShareMessage, callback: WorkerProgressCallback)

}
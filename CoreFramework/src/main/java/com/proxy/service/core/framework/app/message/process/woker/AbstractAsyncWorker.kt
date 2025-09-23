package com.proxy.service.core.framework.app.message.process.woker

import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.channel.ChannelManager
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
        fun onProgress(result: String)

        /**
         * 回调进度
         *
         * @param version   版本号
         * @param result    回调数据
         * */
        fun onProgress(version: String, result: String)

        /**
         * 回调结束
         *
         * @param result    回调数据
         * */
        fun onFinish(result: String)

        /**
         * 回调结束
         *
         * @param version   版本号
         * @param result    回调数据
         * */
        fun onFinish(version: String, result: String)
    }

    /**
     * 获取返回值
     * */
    final override fun getResponse(fromPkg: String, request: ShareMessage): ShareMessage {
        CsTask.mainThread()
            ?.call(object : ICallable<String> {
                override fun accept(): String {
                    doWork(fromPkg, request, object : WorkerProgressCallback {
                        override fun onProgress(result: String) {
                            onProgress(ShareMessageFactory.DEFAULT_VERSION, result)
                        }

                        override fun onProgress(version: String, result: String) {
                            val message = ShareMessageFactory.createResponseProgress(
                                version,
                                request,
                                result
                            )
                            ChannelManager.send(fromPkg, request.receiveChannel, message)
                        }

                        override fun onFinish(result: String) {
                            onFinish(ShareMessageFactory.DEFAULT_VERSION, result)
                        }

                        override fun onFinish(version: String, result: String) {
                            val message = ShareMessageFactory.createResponseFinish(
                                version,
                                request,
                                result
                            )
                            ChannelManager.send(fromPkg, request.receiveChannel, message)
                        }
                    })
                    return ""
                }
            })
            ?.start()
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
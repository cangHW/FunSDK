package com.proxy.service.core.framework.app.message.process.woker

import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory

/**
 * @author: cangHX
 * @data: 2025/9/15 23:03
 * @desc:
 */
abstract class AbstractSyncWorker : AbstractWorker() {

    /**
     * 获取返回值
     * */
    final override fun getResponse(fromPkg: String, request: ShareMessage): ShareMessage {
        return doWork(fromPkg, request)
    }

    /**
     * 执行逻辑, 注意! 该方法执行时, application 不一定完成初始化
     *
     * @param fromPkg   请求方包名
     * @param request   请求信息
     * */
    open fun doWork(fromPkg: String, request: ShareMessage): ShareMessage {
        val result = doWork(request)
        return ShareMessageFactory.createResponseFinish(
            request.messageVersion,
            request,
            result
        )
    }

    /**
     * 执行逻辑, 注意! 该方法执行时, application 不一定完成初始化
     *
     * @param request   请求信息
     * */
    abstract fun doWork(request: ShareMessage): String

}
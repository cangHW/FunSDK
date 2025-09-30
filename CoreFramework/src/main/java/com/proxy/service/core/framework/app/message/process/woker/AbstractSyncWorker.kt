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
     * @param version   数据, 版本号
     * @param result    数据
     * */
    data class Result(val version: String, val result: String)

    /**
     * 获取返回值
     * */
    final override fun getResponse(fromPkg: String, request: ShareMessage): ShareMessage {
        val result = doWork(fromPkg, request)
        return ShareMessageFactory.createResponseFinish(
            result.version,
            request,
            result.result
        )
    }

    /**
     * 执行逻辑, 注意! 该方法执行时, application 不一定完成初始化
     *
     * @param fromPkg   请求方包名
     * @param request   请求信息
     * */
    open fun doWork(fromPkg: String, request: ShareMessage): Result {
        return Result(request.messageVersion, doWork(request))
    }

    /**
     * 执行逻辑, 注意! 该方法执行时, application 不一定完成初始化
     *
     * @param request   请求信息
     * */
    abstract fun doWork(request: ShareMessage): String

}
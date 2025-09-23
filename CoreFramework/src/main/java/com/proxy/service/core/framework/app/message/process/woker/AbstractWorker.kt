package com.proxy.service.core.framework.app.message.process.woker

import com.proxy.service.core.framework.app.message.process.bean.ShareMessage

/**
 * @author: cangHX
 * @data: 2025/9/15 23:03
 * @desc:
 */
abstract class AbstractWorker {

    /**
     * 功能名称
     * */
    abstract fun getMethodName(): String

    /**
     * 获取返回值
     * */
    abstract fun getResponse(fromPkg: String, request: ShareMessage): ShareMessage

}
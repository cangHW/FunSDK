package com.proxy.service.webserver.info.server.factory.base

import fi.iki.elonen.NanoHTTPD.IHTTPSession
import fi.iki.elonen.NanoHTTPD.Response

/**
 * @author: cangHX
 * @date: 2026/6/16 20:03
 * @desc:
 */
abstract class BaseDispatch {

    /**
     * 是否拦截
     * */
    abstract fun intercept(uri: String, session: IHTTPSession): Boolean

    /**
     * 处理
     * */
    abstract fun run(uri: String, session: IHTTPSession): Response

}
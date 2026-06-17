package com.proxy.service.webserver.base.config.builder

/**
 * @author: cangHX
 * @date: 2026/6/15 17:14
 * @desc:
 */
interface IBuilderGet {

    /**
     * 获取 Web 服务监听端口
     */
    fun getPort(): Int
}

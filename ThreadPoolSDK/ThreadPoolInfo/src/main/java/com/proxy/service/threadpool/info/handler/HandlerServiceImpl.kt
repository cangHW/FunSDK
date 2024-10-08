package com.proxy.service.threadpool.info.handler

import com.proxy.service.threadpool.base.handler.HandlerService
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import com.proxy.service.threadpool.info.handler.info.HandlerInfo
import com.proxy.service.threadpool.info.handler.manager.HandlerManager
import com.proxy.service.threadpool.info.handler.option.HandlerOptionImpl

/**
 * @author: cangHX
 * @data: 2024/7/3 18:07
 * @desc:
 */
class HandlerServiceImpl : HandlerService {
    override fun launchTaskGroup(groupName: String): IHandlerOption {
        val handlerInfo = HandlerInfo()
        handlerInfo.handlerController = HandlerManager.getThreadHandler(groupName)
        return HandlerOptionImpl(handlerInfo)
    }
}
package com.proxy.service.threadpool.base.handler

import com.proxy.service.threadpool.base.handler.option.IHandlerOption

/**
 * @author: cangHX
 * @data: 2024/7/3 18:02
 * @desc:
 */
interface HandlerService {


    /**
     * 启动一个子线程任务组，如果当前组存在, 则使用，如果不存在则创建, 同一个组内任务顺序执行
     * */
    fun launchTaskGroup(groupName: String): IHandlerOption

}
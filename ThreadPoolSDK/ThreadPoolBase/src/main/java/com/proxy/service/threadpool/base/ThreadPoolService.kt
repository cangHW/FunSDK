package com.proxy.service.threadpool.base

import com.proxy.service.base.BaseService
import com.proxy.service.threadpool.base.handler.HandlerService
import com.proxy.service.threadpool.base.idle.IdleService
import com.proxy.service.threadpool.base.thread.ThreadService

/**
 * @author: cangHX
 * @date: 2024/5/27 16:28
 * @desc:
 */
interface ThreadPoolService : BaseService, ThreadService, HandlerService, IdleService
package com.proxy.service.threadpool.base.thread.option

import com.proxy.service.threadpool.base.thread.loader.ILoader
import com.proxy.service.threadpool.base.thread.option.base.IAction
import com.proxy.service.threadpool.base.thread.option.base.IObserver
import com.proxy.service.threadpool.base.thread.option.base.IOption
import com.proxy.service.threadpool.base.thread.schedulers.IScheduler

/**
 * @author: cangHX
 * @data: 2024/6/7 16:41
 * @desc:
 */
interface ITaskOption<T> : IObserver<T>, IOption<T>, IAction<T>, IScheduler<ITaskOption<T>>,
    ILoader<T>
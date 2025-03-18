package com.proxy.service.threadpool.info.handler.option

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.handler.loader.IHandlerLoader
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import com.proxy.service.threadpool.info.constants.Constants
import com.proxy.service.threadpool.info.handler.loader.HandlerLoaderImpl
import com.proxy.service.threadpool.info.handler.manager.HandlerController
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/7/3 17:27
 * @desc:
 */
class HandlerOptionImpl(
    private val handlerController: HandlerController
) : HandlerLoaderImpl(handlerController), IHandlerOption {

    companion object {
        private const val TAG = "${Constants.TAG}_HandlerOption"
    }

    override fun setDelay(timeout: Long, unit: TimeUnit): IHandlerLoader {
        delayMillisLocal.set(unit.toMillis(timeout))
        return this
    }

    override fun setAtTime(uptimeMillis: Long): IHandlerLoader {
        uptimeMillisLocal.set(uptimeMillis)
        return this
    }

    override fun getThreadId(): Long {
        return handlerController.getThreadId()
    }

    override fun isGroupDisposed(): Boolean {
        return !handlerController.isCanUse()
    }

    override fun clearAllTask() {
        CsLogger.tag(TAG).i("Thread ${handlerController.getThreadName()} starts to clear all tasks.")
        handlerController.cancelAllTask()
    }

    override fun clearAllTaskWithTag(tag: String) {
        CsLogger.tag(TAG).i("Thread ${handlerController.getThreadName()} starts to clear all tasks with tag. tag = $tag")
        handlerController.cancelTaskByTag(tag)
    }

    override fun disposeGroup() {
        CsLogger.tag(TAG).i("Thread ${handlerController.getThreadName()} starts to disposeGroup.")
        handlerController.close()
    }

    override fun disposeGroupSafely() {
        CsLogger.tag(TAG).i("Thread ${handlerController.getThreadName()} starts to disposeGroupSafely.")
        handlerController.closeSafely()
    }
}
package com.proxy.service.apihttp.info.upload.worker

import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.upload.worker.base.BaseWorker
import com.proxy.service.apihttp.info.upload.worker.task.Task
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.OnCompleteCallback
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IFunction
import java.io.IOException

/**
 * @author: cangHX
 * @data: 2024/12/18 11:13
 * @desc:
 */
class TaskWorker(task: UploadTask) : BaseWorker(task) {

    override fun startTask() {
        CsLogger.tag(tag).i("开始执行任务. taskTag = ${getUploadTask().getTaskTag()}")

        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                //1.任务开始
                callbackStart()
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //2.任务执行
                if (isShouldIntercept(value)) {
                    return value
                }

                try {
                    val response = Task.start(getUploadTask()) {
                        call = it
                    }
                    if (!response.isSuccessful) {
                        callbackEnd(false, IOException("Unexpected code $response"))
                        return TASK_FINISH
                    }
                    callbackEnd(true, null)
                    return TASK_RUNNING
                } catch (throwable: Throwable) {
                    //任务失败
                    callbackEnd(false, throwable)
                    return TASK_FINISH
                }
            }
        })?.setOnCompleteCallback(object : OnCompleteCallback {
            override fun onCallback() {
                //3.任务结束
                finishTask()
            }
        })?.start()
    }

}
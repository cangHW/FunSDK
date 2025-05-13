package com.proxy.service.apihttp.info.download.manager

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.info.download.manager.impl.BoundLifecycleDownloadCallbackImpl
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/11/8 10:43
 * @desc:
 */
object CallbackManager {

    private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}CallbackManager"

    /**
     * 全局下载监听
     * */
    private val globalDownloadCallbackList = ArrayList<DownloadCallback>()

    /**
     * task 任务对应的回调
     * */
    private val lock = Any()
    private val taskDownloadCallbackTemp = WeakHashMap<DownloadCallback, ArrayList<String>>()
    private val taskDownloadCallbackMap = HashMap<String, ArrayList<DownloadCallback>>()

    /**
     * 添加全局回调
     * */
    fun addGlobalDownloadCallback(callback: DownloadCallback) {
        synchronized(globalDownloadCallbackList) {
            if (!globalDownloadCallbackList.contains(callback)) {
                globalDownloadCallbackList.add(callback)
            }
        }
    }

    /**
     * 移除全局回调
     * */
    fun removeGlobalDownloadCallback(callback: DownloadCallback) {
        synchronized(globalDownloadCallbackList) {
            globalDownloadCallbackList.remove(callback)
        }
    }

    /**
     * 添加对应任务回调
     * */
    fun addTaskDownloadCallback(
        taskTag: String,
        callback: DownloadCallback,
        lifecycleOwner: LifecycleOwner?
    ) {
        synchronized(lock) {
            //1. 添加 DownloadCallback
            var callbackList = taskDownloadCallbackMap[taskTag]
            if (callbackList == null) {
                callbackList = ArrayList()
                taskDownloadCallbackMap[taskTag] = callbackList
            }

            var isHas = false
            for (cb in callbackList) {
                isHas = if (cb is BoundLifecycleDownloadCallbackImpl) {
                    cb.isSame(callback)
                } else {
                    (cb == callback)
                }
                if (isHas) {
                    CsLogger.tag(TAG)
                        .i("DownloadCallback 已经存在, 无需重复添加. taskTag = $taskTag, callback = $callback")
                    break
                }
            }
            if (!isHas) {
                if (lifecycleOwner == null) {
                    callbackList.add(callback)
                } else {
                    val impl = BoundLifecycleDownloadCallbackImpl(callback, lifecycleOwner)
                    callbackList.add(impl)
                }
                CsLogger.tag(TAG)
                    .i("DownloadCallback 添加完成. taskTag = $taskTag, callback = $callback")
            }

            //2. 配置 DownloadCallback 索引
            var tagList = taskDownloadCallbackTemp[callback]
            if (tagList == null) {
                tagList = ArrayList()
                taskDownloadCallbackTemp[callback] = tagList
            }
            if (!tagList.contains(taskTag)) {
                tagList.add(taskTag)
            }
        }
    }

    /**
     * 移除对应任务回调
     * */
    fun removeTaskDownloadCallback(callback: DownloadCallback) {
        synchronized(lock) {
            val taskTagList = taskDownloadCallbackTemp[callback]
            if (taskTagList?.isEmpty() == true) {
                CsLogger.tag(TAG).i("该任务不存在回调对象")
                return
            }

            taskTagList?.forEach {
                taskDownloadCallbackMap[it]?.iterator()?.let { iterator ->
                    while (iterator.hasNext()) {
                        val cb = iterator.next()
                        if (cb is BoundLifecycleDownloadCallbackImpl) {
                            if (cb.isSame(callback)) {
                                cb.clearCallback()
                                iterator.remove()
                            }
                        } else {
                            if (cb == callback) {
                                iterator.remove()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 移除对应任务的全部回调
     * */
    fun removeTaskAllDownloadCallback(taskTag: String) {
        synchronized(lock) {
            taskDownloadCallbackMap.remove(taskTag)?.forEach {
                taskDownloadCallbackTemp.remove(it)
            }
        }
    }

    /**
     * 获取当前任务涉及的全部回调
     * */
    fun getDownloadCallbacks(taskTag: String): ArrayList<DownloadCallback> {
        synchronized(lock) {
            val list = ArrayList<DownloadCallback>()
            taskDownloadCallbackMap[taskTag]?.let {
                list.addAll(it)
            }
            list.addAll(globalDownloadCallbackList)
            return list
        }
    }

}
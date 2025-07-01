package com.proxy.service.apihttp.info.upload.manager

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.upload.callback.UploadCallback
import com.proxy.service.apihttp.info.upload.manager.impl.BoundLifecycleUploadCallbackImpl
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/12/19 16:46
 * @desc:
 */
object CallbackManager {

    private const val TAG = "${ApiConstants.LOG_UPLOAD_TAG_START}CallbackManager"

    /**
     * 全局下载监听
     * */
    private val globalUploadCallbackList = ArrayList<UploadCallback>()

    /**
     * task 任务对应的回调
     * */
    private val taskUploadCallbackTemp = WeakHashMap<UploadCallback, ArrayList<String>>()
    private val taskUploadCallbackMap = HashMap<String, ArrayList<UploadCallback>>()

    /**
     * 添加全局回调
     * */
    fun addGlobalUploadCallback(callback: UploadCallback) {
        if (!globalUploadCallbackList.contains(callback)) {
            globalUploadCallbackList.add(callback)
        }
    }

    /**
     * 移除全局回调
     * */
    fun removeGlobalUploadCallback(callback: UploadCallback) {
        globalUploadCallbackList.remove(callback)
    }

    /**
     * 添加对应任务回调
     * */
    fun addTaskUploadCallback(
        taskTag: String,
        callback: UploadCallback,
        lifecycleOwner: LifecycleOwner?
    ) {
        //1. 添加 UploadCallback
        var callbackList = taskUploadCallbackMap[taskTag]
        if (callbackList == null) {
            callbackList = ArrayList()
            taskUploadCallbackMap[taskTag] = callbackList
        }

        var isHas = false
        for (cb in callbackList) {
            isHas = if (cb is BoundLifecycleUploadCallbackImpl) {
                cb.isSame(callback)
            } else {
                (cb == callback)
            }
            if (isHas) {
                CsLogger.tag(TAG)
                    .i("UploadCallback 已经存在, 无需重复添加. taskTag = $taskTag, callback = $callback")
                break
            }
        }
        if (!isHas) {
            if (lifecycleOwner == null) {
                callbackList.add(callback)
            } else {
                val impl = BoundLifecycleUploadCallbackImpl(callback, lifecycleOwner)
                callbackList.add(impl)
            }
            CsLogger.tag(TAG).i("UploadCallback 添加完成. taskTag = $taskTag, callback = $callback")
        }

        //2. 配置 UploadCallback 索引
        var tagList = taskUploadCallbackTemp[callback]
        if (tagList == null) {
            tagList = ArrayList()
            taskUploadCallbackTemp[callback] = tagList
        }
        if (!tagList.contains(taskTag)) {
            tagList.add(taskTag)
        }
    }

    /**
     * 移除对应任务回调
     * */
    fun removeTaskUploadCallback(callback: UploadCallback) {
        val taskTagList = taskUploadCallbackTemp[callback]
        if (taskTagList?.isEmpty() == true) {
            CsLogger.tag(TAG).i("该任务不存在回调对象")
            return
        }
        taskTagList?.forEach { tag ->
            taskUploadCallbackMap[tag]?.let { list ->
                ArrayList<UploadCallback>(list).forEach { cb ->
                    if (cb is BoundLifecycleUploadCallbackImpl) {
                        if (cb.isSame(callback)) {
                            cb.clearCallback()
                            list.remove(cb)
                        }
                    } else {
                        if (cb == callback) {
                            list.remove(cb)
                        }
                    }
                }
            }
        }
    }

    /**
     * 移除对应任务的全部回调
     * */
    fun removeTaskAllUploadCallback(taskTag: String) {
        taskUploadCallbackMap.remove(taskTag)
    }

    /**
     * 获取当前任务涉及的全部回调
     * */
    fun getUploadCallbacks(taskTag: String): ArrayList<UploadCallback> {
        val list = ArrayList<UploadCallback>()
        taskUploadCallbackMap[taskTag]?.let {
            list.addAll(it)
        }
        list.addAll(globalUploadCallbackList)
        return list
    }
}
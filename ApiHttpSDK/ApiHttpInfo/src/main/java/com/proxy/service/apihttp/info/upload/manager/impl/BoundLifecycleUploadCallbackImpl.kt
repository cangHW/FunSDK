package com.proxy.service.apihttp.info.upload.manager.impl

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.proxy.service.apihttp.base.upload.callback.UploadCallback
import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.upload.manager.CallbackManager

/**
 * @author: cangHX
 * @data: 2024/11/8 14:38
 * @desc:
 */
class BoundLifecycleUploadCallbackImpl(
    private var callback: UploadCallback?,
    lifecycleOwner: LifecycleOwner
) : UploadCallback, DefaultLifecycleObserver {

    private val liveData = MutableLiveData<Runnable>()
    private val observer = Observer<Runnable> { t -> t?.run() }

    init {
        liveData.observe(lifecycleOwner, observer)
        lifecycleOwner.lifecycle.addObserver(this)
    }

    /**
     * 校验真实回调对象是否是同一个
     * */
    fun isSame(callback: UploadCallback): Boolean {
        return this.callback == callback
    }

    /**
     * 移除回调
     * */
    fun clearCallback() {
        this.callback = null
        liveData.removeObserver(observer)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        // 退出页面, 自动移除回调
        callback?.let {
            CallbackManager.removeTaskUploadCallback(this)
        }
    }

    override fun onWaiting(task: UploadTask) {
        liveData.postValue(Runnable { callback?.onWaiting(task) })
    }

    override fun onStart(task: UploadTask) {
        liveData.postValue(Runnable { callback?.onStart(task) })
    }

//    override fun onProgress(task: UploadTask, currentSize: Long, progress: Float, speed: Long) {
//        liveData.postValue(Runnable { callback?.onProgress(task, currentSize, progress, speed) })
//    }

    override fun onSuccess(task: UploadTask) {
        liveData.postValue(Runnable { callback?.onSuccess(task) })
    }

    override fun onCancel(task: UploadTask) {
        liveData.postValue(Runnable { callback?.onCancel(task) })
    }

    override fun onFailed(task: UploadTask, throwable: Throwable) {
        liveData.postValue(Runnable { callback?.onFailed(task, throwable) })
    }
}
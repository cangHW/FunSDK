package com.proxy.service.apihttp.info.download.manager.impl

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.proxy.service.apihttp.base.download.callback.DownloadCallback
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.manager.CallbackManager

/**
 * @author: cangHX
 * @data: 2024/11/8 14:38
 * @desc:
 */
class BoundLifecycleDownloadCallbackImpl(
    private var callback: DownloadCallback?,
    lifecycleOwner: LifecycleOwner
) : DownloadCallback, DefaultLifecycleObserver {

    private val liveData = MutableLiveData<Runnable>()
    private val observer = Observer<Runnable> { t -> t?.run() }

    init {
        liveData.observe(lifecycleOwner, observer)
        lifecycleOwner.lifecycle.addObserver(this)
    }

    /**
     * 校验真实回调对象是否是同一个
     * */
    fun isSame(callback: DownloadCallback): Boolean {
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
            CallbackManager.removeTaskDownloadCallback(this)
        }
    }

    override fun onWaiting(task: DownloadTask) {
        liveData.value = Runnable { callback?.onWaiting(task) }
    }

    override fun onStart(task: DownloadTask) {
        liveData.value = Runnable { callback?.onStart(task) }
    }

    override fun onProgress(task: DownloadTask, currentSize: Long, progress: Float, speed: Long) {
        liveData.value = Runnable { callback?.onProgress(task, currentSize, progress, speed) }
    }

    override fun onSuccess(task: DownloadTask) {
        liveData.value = Runnable { callback?.onSuccess(task) }
    }

    override fun onCancel(task: DownloadTask) {
        liveData.value = Runnable { callback?.onCancel(task) }
    }

    override fun onFailed(task: DownloadTask, throwable: Throwable) {
        liveData.value = Runnable { callback?.onFailed(task, throwable) }
    }
}
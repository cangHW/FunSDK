package com.proxy.service.core.framework.io.file.media.base

import android.net.Uri
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/1/2 09:49
 * @desc:
 */
abstract class BaseStore : IMediaStore<BaseStore>, IMediaStore.IInsertAction,
    IMediaStore.IQueryAction {

    protected val tag = "${CoreConfig.TAG}MediaStore_Store"

    protected var uri: Uri? = null
    protected var dirPath: String = ""

    protected var source: ISourceWrite? = null

    protected var displayName: String = ""
    protected var mimeType: String = ""

    /**
     * 操作的 URI
     * */
    fun setUri(uri: Uri): BaseStore {
        this.uri = uri
        return this
    }

    /**
     * 资源路径
     * */
    fun setDir(dirPath: String): BaseStore {
        this.dirPath = dirPath
        return this
    }

    /**
     * 资源
     * */
    fun setSource(source: ISourceWrite): BaseStore {
        this.source = source
        return this
    }

    override fun setDisplayName(displayName: String): BaseStore {
        this.displayName = displayName
        return this
    }

    override fun setMimeType(mimeType: MimeType): BaseStore {
        this.mimeType = mimeType.type
        return this
    }

    /**
     * 回调插入成功
     * */
    protected fun callInsertSuccess(callback: InsertCallback?, path: String) {
        if (callback == null) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onSuccess(path)
                return ""
            }
        })?.start()
    }

    /**
     * 回调插入失败
     * */
    protected fun callInsertFailed(callback: InsertCallback?) {
        if (callback == null) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onFailed()
                return ""
            }
        })?.start()
    }

    /**
     * 回调查询成功
     * */
    protected fun callQuerySuccess(callback: QueryCallback?, list: ArrayList<DataInfo>) {
        if (callback == null) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onSuccess(list)
                return ""
            }
        })?.start()
    }

    /**
     * 回调查询失败
     * */
    protected fun callQueryFailed(callback: QueryCallback?) {
        if (callback == null) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callback.onFailed()
                return ""
            }
        })?.start()
    }
}
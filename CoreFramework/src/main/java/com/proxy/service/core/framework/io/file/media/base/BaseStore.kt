package com.proxy.service.core.framework.io.file.media.base

import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.media.config.StoreType
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.Arrays

/**
 * @author: cangHX
 * @data: 2025/1/2 09:49
 * @desc:
 */
abstract class BaseStore : IMediaStore<BaseStore>, IMediaStore.IInsertAction,
    IMediaStore.IQueryAction {

    companion object {
        const val TAG = "${CoreConfig.TAG}MediaStore_Store"

        const val ID = MediaStore.MediaColumns._ID
        const val DISPLAY_NAME = MediaStore.MediaColumns.DISPLAY_NAME
        const val MIME_TYPE = MediaStore.MediaColumns.MIME_TYPE
        const val SIZE = MediaStore.MediaColumns.SIZE
        const val DATA = MediaStore.MediaColumns.DATA
        const val RELATIVE_PATH = MediaStore.MediaColumns.RELATIVE_PATH
        const val DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED
        const val DATE_MODIFIED = MediaStore.MediaColumns.DATE_MODIFIED
        const val IS_PENDING = MediaStore.MediaColumns.IS_PENDING
        const val IS_TRASHED = MediaStore.MediaColumns.IS_TRASHED
    }

    private var storeType: StoreType? = null
    protected var dirPath: String = ""

    protected var source: ISourceWrite? = null

    protected var displayName: String = ""
    protected var mimeType: String = ""

    /**
     * 资源存储类型
     * */
    fun setStoreType(storeType: StoreType): BaseStore {
        this.storeType = storeType
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
     * 获取操作的文件夹根路径
     * */
    protected fun getRootPath(): String? {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).parentFile?.absolutePath
    }

    /**
     * 获取操作的 URI
     * */
    protected fun getUri(): Uri? {
        return when (storeType) {
            StoreType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            StoreType.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            StoreType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            StoreType.FILE -> MediaStore.Files.getContentUri("external")
            null -> null
        }
    }

    protected abstract class BaseConfig {

        protected var fuzzyMimeType = false

        abstract fun checkSelection(selection: String?): String?

        abstract fun checkSelectionArgs(selectionArgs: Array<String>?): Array<String>?
    }

    protected class DisplayNameConfig(private val displayName: String) : BaseConfig() {

        init {
            fuzzyMimeType = displayName.isEmpty()
        }

        override fun checkSelection(selection: String?): String? {
            if (fuzzyMimeType) {
                return selection
            }
            var str = selection ?: ""
            if (str.isNotEmpty()) {
                str = "$str AND"
            }
            return "$str $DISPLAY_NAME=?"
        }

        override fun checkSelectionArgs(selectionArgs: Array<String>?): Array<String>? {
            if (fuzzyMimeType) {
                return selectionArgs
            }
            if (selectionArgs == null) {
                return arrayOf(displayName)
            }
            val newArray: Array<String> = Arrays.copyOf(selectionArgs, selectionArgs.size + 1)
            newArray[newArray.size - 1] = displayName
            return newArray
        }
    }

    protected class MimeTypeConfig(private val mimeType: String) : BaseConfig() {

        private val isAllowAll: Boolean
        private val prefix: String
        private val postfix: String

        init {
            val split = mimeType.split("/")
            if (split.isEmpty()) {
                prefix = ""
                postfix = ""
            } else if (split.size < 2) {
                prefix = split.get(0)
                postfix = ""
            } else {
                prefix = split.get(0)
                postfix = split.get(1)
            }
            fuzzyMimeType = isPrefixFuzzy() || isPostfixFuzzy()
            isAllowAll = mimeType == "*/*"
        }

        fun isMatches(mimeType: String): Boolean {
            if (isAllowAll) {
                return true
            }
            if (fuzzyMimeType) {
                return mimeType.startsWith(prefix) || mimeType.endsWith(postfix)
            }
            return true
        }

        private fun isPrefixFuzzy(): Boolean {
            return prefix.trim().isEmpty() || prefix == "*"
        }

        private fun isPostfixFuzzy(): Boolean {
            return postfix.trim().isEmpty() || postfix == "*"
        }

        override fun checkSelection(selection: String?): String? {
            if (fuzzyMimeType) {
                return selection
            }
            var str = selection ?: ""
            if (str.isNotEmpty()) {
                str = "$str AND"
            }
            return "$str $MIME_TYPE=?"
        }

        override fun checkSelectionArgs(selectionArgs: Array<String>?): Array<String>? {
            if (fuzzyMimeType) {
                return selectionArgs
            }
            if (selectionArgs == null) {
                return arrayOf(mimeType)
            }
            val newArray: Array<String> = Arrays.copyOf(selectionArgs, selectionArgs.size + 1)
            newArray[newArray.size - 1] = mimeType
            return newArray
        }
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
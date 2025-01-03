package com.proxy.service.core.framework.io.file.media.store

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.base.BaseStore
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.OutputStream
import java.util.Arrays


/**
 * @author: cangHX
 * @data: 2025/1/2 09:48
 * @desc:
 */
class SupportV29Store : BaseStore() {

    companion object {
        private const val ID = MediaStore.MediaColumns._ID
        private const val DISPLAY_NAME = MediaStore.MediaColumns.DISPLAY_NAME
        private const val MIME_TYPE = MediaStore.MediaColumns.MIME_TYPE
        private const val DATA = MediaStore.MediaColumns.DATA
        private const val RELATIVE_PATH = MediaStore.MediaColumns.RELATIVE_PATH
        private const val DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED
        private const val DATE_MODIFIED = MediaStore.MediaColumns.DATE_MODIFIED
        private const val IS_PENDING = MediaStore.MediaColumns.IS_PENDING
        private const val IS_TRASHED = MediaStore.MediaColumns.IS_TRASHED
    }

    override fun insert(callback: InsertCallback?) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val finalSource = source
                if (finalSource == null) {
                    CsLogger.tag(tag).e("source is null")
                    callInsertFailed(callback)
                    return ""
                }

                val finalUri = uri
                if (finalUri == null) {
                    CsLogger.tag(tag).e("uri is null")
                    callInsertFailed(callback)
                    return ""
                }

                val values = createContentValues()
                val cr = CsContextManager.getApplication().contentResolver
                val uri: Uri? = cr.insert(finalUri, values)
                if (uri == null) {
                    CsLogger.tag(tag).e("insert error")
                    callInsertFailed(callback)
                    return ""
                }

                var stream: OutputStream? = null
                try {
                    stream = cr.openOutputStream(uri)
                    if (stream == null) {
                        CsLogger.tag(tag).e("open outputStream error")
                        callInsertFailed(callback)
                        return ""
                    }
                    if (finalSource.write(stream)) {
                        CsLogger.tag(tag).d("insert success")

                        values.clear()
                        values.put(IS_PENDING, 0)
                        cr.update(uri, values, null, null)

                        var path = ""
                        val projection = arrayOf(DATA)
                        cr.query(uri, projection, null, null, null)
                            .use { cursor ->
                                if (cursor != null && cursor.moveToFirst()) {
                                    path = getValueFromCursor(cursor, DATA, "")
                                }
                            }

                        callInsertSuccess(callback, path)
                    } else {
                        CsLogger.tag(tag).e("write error")
                        callInsertFailed(callback)
                    }
                } catch (throwable: Throwable) {
                    cr.delete(uri, null, null)
                    CsLogger.tag(tag).e(throwable)
                    callInsertFailed(callback)
                } finally {
                    CsFileUtils.close(stream)
                }
                return ""
            }
        })?.start()
    }

    override fun query(callback: QueryCallback?) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val finalUri = uri
                if (finalUri == null) {
                    CsLogger.tag(tag).e("uri is null")
                    callQueryFailed(callback)
                    return ""
                }

                // 定义要查询的列
                val projection = arrayOf(
                    ID,
                    DISPLAY_NAME,
                    MIME_TYPE,
                    RELATIVE_PATH,
                    DATA,
                    DATE_ADDED,
                    IS_PENDING,
                    IS_TRASHED
                )

                // 定义筛选条件
                var selection: String? = null
                var selectionArgs: Array<String>? = null

                val displayNameConfig = DisplayNameConfig(displayName)
                selection = displayNameConfig.checkSelection(selection)
                selectionArgs = displayNameConfig.checkSelectionArgs(selectionArgs)

                val mimeTypeConfig = MimeTypeConfig(mimeType)
                selection = mimeTypeConfig.checkSelection(selection)
                selectionArgs = mimeTypeConfig.checkSelectionArgs(selectionArgs)

                // 定义排序顺序
                val sortOrder = "$DATE_ADDED DESC"
                val contentResolver = CsContextManager.getApplication().contentResolver

                val cursor = contentResolver.query(
                    finalUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )

                if (cursor == null) {
                    CsLogger.tag(tag).e("cursor is null")
                    callQueryFailed(callback)
                    return ""
                }

                val list = ArrayList<DataInfo>()
                cursor.use {
                    while (cursor.moveToNext()) {
                        val mimeType = getValueFromCursor(cursor, MIME_TYPE, "")
                        if (!mimeTypeConfig.isMatches(mimeType)) {
                            continue
                        }

                        val data = DataInfo()
                        data.id = getValueFromCursor(cursor, ID, -1L)
                        data.displayName = getValueFromCursor(cursor, DISPLAY_NAME, "")
                        data.mimeType = mimeType
                        data.path = getValueFromCursor(cursor, DATA, "")
                        data.relativePath = getValueFromCursor(cursor, RELATIVE_PATH, "")
                        data.dateAdded = getValueFromCursor(cursor, DATE_ADDED, -1L)
                        data.isPending = getValueFromCursor(cursor, IS_PENDING, 0)
                        data.isTrashed = getValueFromCursor(cursor, IS_TRASHED, 0)

                        CsLogger.tag(tag).d(data.toString())

                        list.add(data)
                    }
                }
                callQuerySuccess(callback, list)
                return ""
            }
        })?.start()
    }

    private fun getValueFromCursor(cursor: Cursor, columnName: String, defaultValue: Long): Long {
        try {
            val column: Int = cursor.getColumnIndexOrThrow(columnName)
            return cursor.getLong(column)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return defaultValue
    }

    private fun getValueFromCursor(cursor: Cursor, columnName: String, defaultValue: Int): Int {
        try {
            val column: Int = cursor.getColumnIndexOrThrow(columnName)
            return cursor.getInt(column)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return defaultValue
    }

    private fun getValueFromCursor(
        cursor: Cursor,
        columnName: String,
        defaultValue: String
    ): String {
        try {
            val column: Int = cursor.getColumnIndexOrThrow(columnName)
            return cursor.getString(column)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return defaultValue
    }

    /**
     * 创建 ContentValues
     * */
    private fun createContentValues(): ContentValues {
        val now = System.currentTimeMillis()

        val values = ContentValues()
        values.put(DISPLAY_NAME, displayName)
        values.put(MIME_TYPE, mimeType)
        values.put(DATE_ADDED, now / 1000)
        values.put(DATE_MODIFIED, now / 1000)
        values.put(RELATIVE_PATH, dirPath)
        values.put(IS_PENDING, 1)
        return values
    }

    private abstract class BaseConfig {

        protected var fuzzyMimeType = false

        abstract fun checkSelection(selection: String?): String?

        abstract fun checkSelectionArgs(selectionArgs: Array<String>?): Array<String>?
    }

    private class DisplayNameConfig(val displayName: String) : BaseConfig() {

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

    private class MimeTypeConfig(val mimeType: String) : BaseConfig() {

        private val isAllowAll: Boolean
        private val prefix: String
        private val postfix: String

        init {
            val split = mimeType.split("/")
            if (split.size < 1) {
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
}
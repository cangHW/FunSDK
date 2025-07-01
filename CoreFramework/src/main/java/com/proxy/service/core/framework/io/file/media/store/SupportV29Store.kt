package com.proxy.service.core.framework.io.file.media.store

import android.content.ContentValues
import android.net.Uri
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.media.base.BaseStore
import com.proxy.service.core.framework.io.file.media.callback.InsertCallback
import com.proxy.service.core.framework.io.file.media.callback.QueryCallback
import com.proxy.service.core.framework.io.file.media.config.DataInfo
import com.proxy.service.core.framework.io.file.media.utils.CursorUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.OutputStream

/**
 * @author: cangHX
 * @data: 2025/1/2 09:48
 * @desc:
 */
class SupportV29Store : BaseStore() {

    init {
        CsLogger.tag(TAG).d("SupportV29Store start")
    }

    override fun insert(callback: InsertCallback?) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val finalSource = source
                if (finalSource == null) {
                    CsLogger.tag(TAG).e("source is null")
                    callInsertFailed(callback)
                    return ""
                }

                val finalUri = getUri()
                if (finalUri == null) {
                    CsLogger.tag(TAG).e("uri is null")
                    callInsertFailed(callback)
                    return ""
                }

                val values = createContentValues()
                val cr = CsContextManager.getApplication().contentResolver
                val uri: Uri? = cr.insert(finalUri, values)
                if (uri == null) {
                    CsLogger.tag(TAG).e("insert error")
                    callInsertFailed(callback)
                    return ""
                }

                var stream: OutputStream? = null
                try {
                    stream = cr.openOutputStream(uri)
                    if (stream == null) {
                        CsLogger.tag(TAG).e("open outputStream error")
                        callInsertFailed(callback)
                        return ""
                    }
                    if (finalSource.write(stream)) {
                        CsLogger.tag(TAG).d("insert success")

                        values.clear()
                        values.put(IS_PENDING, 0)
                        cr.update(uri, values, null, null)

                        var path = ""
                        val projection = arrayOf(DATA)
                        cr.query(uri, projection, null, null, null)
                            .use { cursor ->
                                if (cursor != null && cursor.moveToFirst()) {
                                    path = CursorUtils.getValueFromCursor(cursor, DATA, "")
                                }
                            }

                        callInsertSuccess(callback, path)
                    } else {
                        CsLogger.tag(TAG).e("write error")
                        callInsertFailed(callback)
                    }
                } catch (throwable: Throwable) {
                    cr.delete(uri, null, null)
                    CsLogger.tag(TAG).e(throwable)
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
                val finalUri = getUri()
                if (finalUri == null) {
                    CsLogger.tag(TAG).e("uri is null")
                    callQueryFailed(callback)
                    return ""
                }

                // 定义要查询的列
                val projection = arrayOf(
                    ID,
                    DISPLAY_NAME,
                    MIME_TYPE,
                    SIZE,
                    RELATIVE_PATH,
                    DATA,
                    DATE_ADDED,
                    DATE_MODIFIED,
                    IS_PENDING,
                    IS_TRASHED
                )

                val displayNameConfig = DisplayNameConfig(displayName)
                val mimeTypeConfig = MimeTypeConfig(mimeType)

                // 定义筛选条件
                var selection: String? = displayNameConfig.checkSelection(null)
                var selectionArgs: Array<String>? = displayNameConfig.checkSelectionArgs(null)

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
                    CsLogger.tag(TAG).e("cursor is null")
                    callQueryFailed(callback)
                    return ""
                }

                val list = ArrayList<DataInfo>()
                cursor.use {
                    while (cursor.moveToNext()) {
                        val mimeType = CursorUtils.getValueFromCursor(cursor, MIME_TYPE, "")
                        if (!mimeTypeConfig.isMatches(mimeType)) {
                            continue
                        }

                        val data = DataInfo()
                        data.id = CursorUtils.getValueFromCursor(cursor, ID, -1L)
                        data.displayName = CursorUtils.getValueFromCursor(cursor, DISPLAY_NAME, "")
                        data.mimeType = mimeType
                        data.size = CursorUtils.getValueFromCursor(cursor, SIZE, 0L)
                        data.rootDir = getRootPath() ?: ""
                        data.path = CursorUtils.getValueFromCursor(cursor, DATA, "")
                        data.relativePath =
                            CursorUtils.getValueFromCursor(cursor, RELATIVE_PATH, "")
                        data.dateAdded = CursorUtils.getValueFromCursor(cursor, DATE_ADDED, -1L)
                        data.dateModified =
                            CursorUtils.getValueFromCursor(cursor, DATE_MODIFIED, -1L)
                        data.isPending = CursorUtils.getValueFromCursor(cursor, IS_PENDING, 0)
                        data.isTrashed = CursorUtils.getValueFromCursor(cursor, IS_TRASHED, 0)

                        CsLogger.tag(TAG).d(data.toString())
                        list.add(data)
                    }
                }
                callQuerySuccess(callback, list)
                return ""
            }
        })?.start()
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
}